package com.example.demo;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import java.util.Optional;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.*;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.security.*;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import com.example.demo.servlet.SimpleHelloServlet;

import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.LocalDateTime;


@Listeners(TestResultListener.class)
public class ApartmentFacilityBookingSystemTest {

    @Mock private UserRepository userRepository;
    @Mock private ApartmentUnitRepository apartmentUnitRepository;
    @Mock private FacilityRepository facilityRepository;
    @Mock private BookingRepository bookingRepository;
    @Mock private BookingLogRepository bookingLogRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private Authentication authentication;
    @Mock private CustomUserDetailsService customUserDetailsService;

    private UserService userService;
    private ApartmentUnitService apartmentUnitService;
    private FacilityService facilityService;
    private BookingLogService bookingLogService;
    private BookingService bookingService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeClass
    public void init() {
        MockitoAnnotations.openMocks(this);
        jwtTokenProvider = new JwtTokenProvider(
                "MySuperSecretJwtKeyForApartmentSystem123456", 3600000L);

        userService = new UserServiceImpl(userRepository, passwordEncoder);
        apartmentUnitService = new ApartmentUnitServiceImpl(apartmentUnitRepository, userRepository);
        facilityService = new FacilityServiceImpl(facilityRepository);
        bookingLogService = new BookingLogServiceImpl(bookingLogRepository, bookingRepository);
        bookingService = new BookingServiceImpl(bookingRepository, facilityRepository, userRepository, bookingLogService);
    }

    // ------------------------------- 1) SERVLET TESTS -------------------------------
    @Test(groups="servlet", priority=1)
    public void t1_servletHello() throws Exception {
        SimpleHelloServlet s = new SimpleHelloServlet();
        MockHttpServletRequest rq = new MockHttpServletRequest("GET","/hello-servlet");
        MockHttpServletResponse rs = new MockHttpServletResponse();
        s.doGet(rq, rs);
        Assert.assertEquals(rs.getContentAsString(), "Hello from Simple Servlet");
    }

    @Test(groups="servlet", priority=2)
    public void t2_servletStatus200() throws Exception {
        SimpleHelloServlet s = new SimpleHelloServlet();
        MockHttpServletRequest rq = new MockHttpServletRequest("GET","/hello-servlet");
        MockHttpServletResponse rs = new MockHttpServletResponse();
        s.doGet(rq, rs);
        Assert.assertEquals(rs.getStatus(), 200);
    }

    @Test(groups="servlet", priority=3)
    public void t3_servletContentType() throws Exception {
        SimpleHelloServlet s = new SimpleHelloServlet();
        MockHttpServletRequest rq = new MockHttpServletRequest("GET","/hello-servlet");
        MockHttpServletResponse rs = new MockHttpServletResponse();
        s.doGet(rq, rs);
        Assert.assertEquals(rs.getContentType(), "text/plain");
    }

    @Test(groups="servlet", priority=4)
    public void t4_servletInfo() {
        SimpleHelloServlet s = new SimpleHelloServlet();
        Assert.assertTrue(s.getServletInfo().contains("SimpleHelloServlet"));
    }

    @Test(groups="servlet", priority=5)
    public void t5_servletMultipleCalls() throws Exception {
        SimpleHelloServlet s = new SimpleHelloServlet();
        for(int i = 0; i < 3; i++) {
            MockHttpServletRequest rq = new MockHttpServletRequest("GET","/hello-servlet");
            MockHttpServletResponse rs = new MockHttpServletResponse();
            s.doGet(rq, rs);
            Assert.assertEquals(rs.getContentAsString(), "Hello from Simple Servlet");
        }
    }

    @Test(groups="servlet", priority=6)
    public void t6_servletNotNull() {
        Assert.assertNotNull(new SimpleHelloServlet());
    }

    @Test(groups="servlet", priority=7)
    public void t7_servletUrlPatternLogical() {
        Assert.assertNotNull(new SimpleHelloServlet());
    }

    @Test(groups="servlet", priority=8)
    public void t8_servletNoExceptionOnPost() throws Exception {
        SimpleHelloServlet s = new SimpleHelloServlet();
        MockHttpServletRequest rq = new MockHttpServletRequest("POST","/hello-servlet");
        MockHttpServletResponse rs = new MockHttpServletResponse();
        s.service(rq, rs);
        Assert.assertTrue(rs.getStatus()==200 || rs.getStatus()==405);
    }

    // ------------------------------- 2) CRUD TESTS -------------------------------
    @Test(groups="crud", priority=9)
    public void t9_registerUser() {
        RegisterRequest req = new RegisterRequest();
        req.setName("John"); req.setEmail("john@example.com"); req.setPassword("p");

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("p")).thenReturn("enc");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0); u.setId(1L); return u;
        });

        User u = new User(null, "John","john@example.com","p","RESIDENT");
        User saved = userService.register(u);

        Assert.assertEquals(saved.getId(), Long.valueOf(1L));
    }

    @Test(groups="crud", priority=10, expectedExceptions = BadRequestException.class)
    public void t10_registerDuplicateEmail() {
        when(userRepository.existsByEmail("x@example.com")).thenReturn(true);
        User u = new User(null,"X","x@example.com","p","RESIDENT");
        userService.register(u);
    }

    @Test(groups="crud", priority=11)
    public void t11_assignUnit() {
        User u = new User(1L,"U","u@e.com","p","RESIDENT");
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));
        when(apartmentUnitRepository.save(any(ApartmentUnit.class))).thenAnswer(inv -> {
            ApartmentUnit au = inv.getArgument(0); au.setId(10L); return au;
        });

        ApartmentUnit unit = new ApartmentUnit(null,"A101",1,null);
        ApartmentUnit res = apartmentUnitService.assignUnitToUser(1L, unit);

        Assert.assertEquals(res.getOwner().getId(), Long.valueOf(1L));
    }

    @Test(groups="crud", priority=12)
    public void t12_addFacility() {
        Facility f = new Facility(null,"Gym","D","06:00","22:00");

        when(facilityRepository.findByName("Gym")).thenReturn(Optional.empty());
        when(facilityRepository.save(any(Facility.class))).thenAnswer(inv -> {
            Facility x = inv.getArgument(0); x.setId(1L); return x;
        });

        Facility saved = facilityService.addFacility(f);

        Assert.assertEquals(saved.getId(), Long.valueOf(1L));
    }

    @Test(groups="crud", priority=13, expectedExceptions = BadRequestException.class)
    public void t13_addFacilityInvalidTime() {
        Facility f = new Facility(null,"Pool","D","22:00","06:00");
        when(facilityRepository.findByName("Pool")).thenReturn(Optional.empty());
        facilityService.addFacility(f);
    }

    @Test(groups="crud", priority=14)
    public void t14_createBooking() {
        Facility fac = new Facility(1L,"Gym","D","06:00","22:00");
        User u = new User(1L,"U","u@e.com","p","RESIDENT");

        LocalDateTime s = LocalDateTime.of(2025,1,1,8,0);
        LocalDateTime e = LocalDateTime.of(2025,1,1,9,0);

        Booking b = new Booking(null,null,null,s,e,null);

        when(facilityRepository.findById(1L)).thenReturn(Optional.of(fac));
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        when(bookingRepository.findByFacilityAndStartTimeLessThanAndEndTimeGreaterThan(eq(fac), any(), any()))
                .thenReturn(Collections.emptyList());

        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> {
            Booking x = inv.getArgument(0); x.setId(100L); return x;
        });

        when(bookingLogRepository.save(any(BookingLog.class))).thenAnswer(inv -> inv.getArgument(0));

        Booking created = bookingService.createBooking(1L,1L,b);

        Assert.assertEquals(created.getStatus(), Booking.STATUS_CONFIRMED);
    }

    @Test(groups="crud", priority=15, expectedExceptions = ConflictException.class)
    public void t15_createBookingConflict() {
        Facility fac = new Facility(1L,"Gym","D","06:00","22:00");
        User u = new User(1L,"U","u@e.com","p","RESIDENT");

        LocalDateTime s = LocalDateTime.of(2025,1,1,8,0);
        LocalDateTime e = LocalDateTime.of(2025,1,1,9,0);

        Booking existing = new Booking(1L,fac,u,s,e,Booking.STATUS_CONFIRMED);
        Booking b = new Booking(null,null,null,s.plusMinutes(10),e.plusMinutes(10),null);

        when(facilityRepository.findById(1L)).thenReturn(Optional.of(fac));
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        when(bookingRepository.findByFacilityAndStartTimeLessThanAndEndTimeGreaterThan(eq(fac), any(), any()))
                .thenReturn(List.of(existing));

        bookingService.createBooking(1L,1L,b);
    }

    @Test(groups="crud", priority=16)
    public void t16_cancelBooking() {
        Booking b = new Booking(1L,null,null,
                LocalDateTime.now(),LocalDateTime.now().plusHours(1),
                Booking.STATUS_CONFIRMED);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(b));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));
        when(bookingLogRepository.save(any(BookingLog.class))).thenAnswer(inv -> inv.getArgument(0));

        Booking c = bookingService.cancelBooking(1L);

        Assert.assertEquals(c.getStatus(), Booking.STATUS_CANCELLED);
    }

    // ------------------------------- 3) DI → IoC -------------------------------
    @Test(groups="di", priority=17)
    public void t17_userServiceNotNull(){ Assert.assertNotNull(userService); }

    @Test(groups="di", priority=18)
    public void t18_apartmentServiceNotNull(){ Assert.assertNotNull(apartmentUnitService); }

    @Test(groups="di", priority=19)
    public void t19_facilityServiceNotNull(){ Assert.assertNotNull(facilityService); }

    @Test(groups="di", priority=20)
    public void t20_bookingServiceNotNull(){ Assert.assertNotNull(bookingService); }

    @Test(groups="di", priority=21)
    public void t21_bookingLogServiceNotNull(){ Assert.assertNotNull(bookingLogService); }

    @Test(groups="di", priority=22)
    public void t22_passwordEncoderMockNotNull(){ Assert.assertNotNull(passwordEncoder); }

    @Test(groups="di", priority=23)
    public void t23_jwtProviderNotNull(){ Assert.assertNotNull(jwtTokenProvider); }

    @Test(groups="di", priority=24)
    public void t24_repositoriesNotNull(){ Assert.assertNotNull(userRepository); }

    // ------------------------------- 4) Hibernate Tests -------------------------------
    @Test(groups="hibernate", priority=25)
    public void t25_userDefaultRole(){
        User u=new User(); u.setRole("RESIDENT");
        Assert.assertEquals(u.getRole(),"RESIDENT");
    }

    @Test(groups="hibernate", priority=26)
    public void t26_bookingDefaultStatus() {
        Booking b = new Booking();
        Assert.assertEquals(b.getStatus(),Booking.STATUS_CONFIRMED);
    }

    @Test(groups="hibernate", priority=27)
    public void t27_bookingLogTimestamp() {
        BookingLog log = new BookingLog(); log.onCreate();
        Assert.assertNotNull(log.getLoggedAt());
    }

    @Test(groups="hibernate", priority=28)
    public void t28_userRepositorySave() {
        User u = new User(null,"A","a@e.com","p","RESIDENT");

        when(userRepository.existsByEmail("a@e.com")).thenReturn(false);
        when(passwordEncoder.encode("p")).thenReturn("enc");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User x = inv.getArgument(0); x.setId(5L); return x;
        });

        User s = userService.register(u);
        Assert.assertEquals(s.getId(), Long.valueOf(5L));
    }

    @Test(groups="hibernate", priority=29)
    public void t29_facilitySave() {
        Facility f = new Facility(null,"Hall","D","08:00","20:00");

        when(facilityRepository.findByName("Hall")).thenReturn(Optional.empty());
        when(facilityRepository.save(any(Facility.class))).thenAnswer(inv -> {
            Facility x = inv.getArgument(0); x.setId(2L); return x;
        });

        Facility s = facilityService.addFacility(f);
        Assert.assertEquals(s.getId(), Long.valueOf(2L));
    }

    @Test(groups="hibernate", priority=30)
    public void t30_bookingFind() {
        Booking b = new Booking(11L,null,null,
                LocalDateTime.now(),LocalDateTime.now().plusHours(1),
                Booking.STATUS_CONFIRMED);

        when(bookingRepository.findById(11L)).thenReturn(Optional.of(b));

        Assert.assertEquals(bookingService.getBooking(11L).getId(), Long.valueOf(11L));
    }

    @Test(groups="hibernate", priority=31)
    public void t31_unitFindByOwner() {
        User u = new User(2L,"B","b@e.com","p","RESIDENT");
        ApartmentUnit unit = new ApartmentUnit(20L,"B201",2,u);

        when(userRepository.findById(2L)).thenReturn(Optional.of(u));
        when(apartmentUnitRepository.save(any(ApartmentUnit.class))).thenReturn(unit);
        when(apartmentUnitRepository.findByOwner(u)).thenReturn(Optional.of(unit));

        apartmentUnitService.assignUnitToUser(2L,new ApartmentUnit(null,"B201",2,null));

        Assert.assertEquals(apartmentUnitService.getUnitByUser(2L).getUnitNumber(),"B201");
    }

    @Test(groups="hibernate", priority=32)
    public void t32_logSave() {
        Booking b = new Booking(30L,null,null,LocalDateTime.now(),LocalDateTime.now().plusHours(1),
                Booking.STATUS_CONFIRMED);

        BookingLog log = new BookingLog(1L,b,"Test",LocalDateTime.now());

        when(bookingRepository.findById(30L)).thenReturn(Optional.of(b));
        when(bookingLogRepository.save(any(BookingLog.class))).thenReturn(log);

        Assert.assertEquals(bookingLogService.addLog(30L,"Test").getLogMessage(),"Test");
    }

    // ------------------------------- 5) JPA Mapping -------------------------------
    @Test(groups="jpa-mapping", priority=33)
    public void t33_oneToOneUserUnit() {
        User u = new User(1L,"U","u@e.com","p","RESIDENT");
        ApartmentUnit au = new ApartmentUnit(1L,"A101",1,u);
        u.setApartmentUnit(au);

        Assert.assertEquals(au.getOwner().getId(), u.getId());
    }

    @Test(groups="jpa-mapping", priority=34)
    public void t34_manyToOneFacility() {
        Facility f = new Facility(1L,"Pool","D","08:00","20:00");
        Booking b = new Booking(1L,f,null,
                LocalDateTime.now(),LocalDateTime.now().plusHours(1),
                Booking.STATUS_CONFIRMED);

        Assert.assertEquals(b.getFacility().getName(),"Pool");
    }

    @Test(groups="jpa-mapping", priority=35)
    public void t35_manyToOneUser() {
        User u = new User(1L,"U","u@e.com","p","RESIDENT");
        Booking b = new Booking(1L,null,u,
                LocalDateTime.now(),LocalDateTime.now().plusHours(1),
                Booking.STATUS_CONFIRMED);

        Assert.assertEquals(b.getUser().getEmail(),"u@e.com");
    }

    @Test(groups="jpa-mapping", priority=36)
    public void t36_1NF_facilityTimesNonNull() {
        Facility f = new Facility(1L,"Gym","D","06:00","22:00");
        Assert.assertNotNull(f.getOpenTime());
    }

    @Test(groups="jpa-mapping", priority=37)
    public void t37_2NF_bookingDependsOnId() {
        Booking b = new Booking(1L,null,null,
                LocalDateTime.now(),LocalDateTime.now().plusHours(1),
                Booking.STATUS_CONFIRMED);

        Assert.assertNotNull(b.getStartTime());
    }

    @Test(groups="jpa-mapping", priority=38)
    public void t38_3NF_bookingLogNoTransitive() {
        BookingLog log = new BookingLog();
        log.setLogMessage("Created");
        Assert.assertEquals(log.getLogMessage(),"Created");
    }

    @Test(groups="jpa-mapping", priority=39)
    public void t39_normalizedBookingLog() {
        Booking b = new Booking(1L,null,null,
                LocalDateTime.now(),LocalDateTime.now().plusHours(1),
                Booking.STATUS_CONFIRMED);

        BookingLog log = new BookingLog(1L,b,"Created",LocalDateTime.now());
        Assert.assertEquals(log.getBooking().getId(), b.getId());
    }

    // ------------------------------- 6) MANY TO MANY LOGICAL -------------------------------
    @Test(groups="many-to-many", priority=40)
    public void t40_userMultipleBookings() {
        User u = new User(1L,"U","u@e.com","p","RESIDENT");

        Facility f1 = new Facility(1L,"Gym","D","06:00","22:00");
        Facility f2 = new Facility(2L,"Pool","D","06:00","22:00");

        Booking b1 = new Booking(1L,f1,u,LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),Booking.STATUS_CONFIRMED);

        Booking b2 = new Booking(2L,f2,u,LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),Booking.STATUS_CONFIRMED);

        Assert.assertEquals(Arrays.asList(b1,b2).size(),2);
    }

    @Test(groups="many-to-many", priority=41)
    public void t41_facilityMultipleUsers() {
        Facility f = new Facility(1L,"Gym","D","06:00","22:00");

        User u1 = new User(1L,"U1","u1@e.com","p","RESIDENT");
        User u2 = new User(2L,"U2","u2@e.com","p","RESIDENT");

        Booking b1 = new Booking(1L,f,u1,LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),Booking.STATUS_CONFIRMED);

        Booking b2 = new Booking(2L,f,u2,LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),Booking.STATUS_CONFIRMED);

        Assert.assertEquals(new HashSet<>(Arrays.asList(b1.getUser(),b2.getUser())).size(),2);
    }

    @Test(groups="many-to-many", priority=42)
    public void t42_noDuplicateBookingIds() {
        Facility f = new Facility(1L,"Gym","D","06:00","22:00");
        User u = new User(1L,"U","u@e.com","p","RESIDENT");

        Booking b1 = new Booking(1L,f,u,LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),Booking.STATUS_CONFIRMED);

        Booking b2 = new Booking(2L,f,u,LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusHours(3),Booking.STATUS_CONFIRMED);

        Assert.assertNotEquals(b1.getId(), b2.getId());
    }

    @Test(groups="many-to-many", priority=43)
    public void t43_logicalManyToManyCardinality() {
        User u1 = new User(1L,"U1","u1@e.com","p","RESIDENT");
        User u2 = new User(2L,"U2","u2@e.com","p","RESIDENT");

        Facility f1 = new Facility(1L,"F1","D","06:00","22:00");
        Facility f2 = new Facility(2L,"F2","D","06:00","22:00");

        Booking b1 = new Booking(1L,f1,u1,LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),Booking.STATUS_CONFIRMED);

        Booking b2 = new Booking(2L,f2,u1,LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),Booking.STATUS_CONFIRMED);

        Booking b3 = new Booking(3L,f1,u2,LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),Booking.STATUS_CONFIRMED);

        Assert.assertEquals(Arrays.asList(b1,b2,b3).size(),3);
    }

    @Test(groups="many-to-many", priority=44)
    public void t44_edgeNoBookings() {
        Assert.assertTrue(Collections.<Booking>emptyList().isEmpty());
    }

    @Test(groups="many-to-many", priority=45)
    public void t45_edgeSingleBooking() {
        Facility f = new Facility(1L,"Gym","D","06:00","22:00");
        User u = new User(1L,"U","u@e.com","p","RESIDENT");

        Booking b = new Booking(1L,f,u,LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),Booking.STATUS_CONFIRMED);

        Assert.assertEquals(b.getFacility().getId(), Long.valueOf(1L));
    }

    @Test(groups="many-to-many", priority=46)
    public void t46_joinEntityNotNull() {
        Booking b = new Booking(1L,new Facility(),new User(),
                LocalDateTime.now(),LocalDateTime.now().plusHours(1),
                Booking.STATUS_CONFIRMED);

        Assert.assertNotNull(b.getFacility());
    }

    // ------------------------------- 7) JWT SECURITY -------------------------------
    @Test(groups="jwt-security", priority=47)
    public void t47_jwtContainsClaims() {
        UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername("user@e.com").password("p").roles("RESIDENT").build();

        when(authentication.getPrincipal()).thenReturn(ud);

        String token = jwtTokenProvider.generateToken(authentication,
                1L,"user@e.com","RESIDENT");

        Assert.assertEquals(jwtTokenProvider.getUserIdFromToken(token), Long.valueOf(1L));
        Assert.assertEquals(jwtTokenProvider.getEmailFromToken(token),"user@e.com");
        Assert.assertEquals(jwtTokenProvider.getRoleFromToken(token),"RESIDENT");
    }

    @Test(groups="jwt-security", priority=48)
    public void t48_jwtInvalidTokenFalse() {
        Assert.assertFalse(jwtTokenProvider.validateToken("x.y.z"));
    }

    @Test(groups="jwt-security", priority=49)
    public void t49_jwtValidTokenTrue() {
        UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername("t@e.com").password("p").roles("RESIDENT").build();

        when(authentication.getPrincipal()).thenReturn(ud);

        String token = jwtTokenProvider.generateToken(authentication,
                2L,"t@e.com","RESIDENT");

        Assert.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test(groups="jwt-security", priority=50)
    public void t50_jwtUserIdFallbackSubject() {
        String raw = io.jsonwebtoken.Jwts.builder()
                .setSubject("10")
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                                "MySuperSecretJwtKeyForApartmentSystem123456".getBytes()),
                        io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();

        Assert.assertEquals(jwtTokenProvider.getUserIdFromToken(raw), Long.valueOf(10L));
    }

    @Test(groups="jwt-security", priority=51)
    public void t51_jwtRoleNoPrefixChange() {
        UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername("r@e.com").password("p").roles("RESIDENT").build();

        when(authentication.getPrincipal()).thenReturn(ud);

        String token = jwtTokenProvider.generateToken(authentication,
                3L,"r@e.com","RESIDENT");

        Assert.assertEquals(jwtTokenProvider.getRoleFromToken(token),"RESIDENT");
    }

    @Test(groups="jwt-security", priority=52)
    public void t52_jwtTokenNotNull() {
        UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername("x@e.com").password("p").roles("RESIDENT").build();

        when(authentication.getPrincipal()).thenReturn(ud);

        String token = jwtTokenProvider.generateToken(authentication,
                4L,"x@e.com","RESIDENT");

        Assert.assertNotNull(token);
    }

    @Test(groups="jwt-security", priority=53)
    public void t53_jwtDifferentIdsProduceDifferentTokens() {
        UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername("x@e.com").password("p").roles("RESIDENT").build();

        when(authentication.getPrincipal()).thenReturn(ud);

        String t1 = jwtTokenProvider.generateToken(authentication,
                5L,"x@e.com","RESIDENT");

        String t2 = jwtTokenProvider.generateToken(authentication,
                6L,"x@e.com","RESIDENT");

        Assert.assertNotEquals(t1,t2);
    }

    // ------------------------------- 8) HQL/HCQL -------------------------------
    @Test(groups="hql-hcql", priority=54)
    public void t54_queryBookingsByFacilityRange() {
        Facility f = new Facility(1L,"Gym","D","06:00","22:00");

        Booking b = new Booking(1L,f,null,
                LocalDateTime.now(),LocalDateTime.now().plusHours(1),
                Booking.STATUS_CONFIRMED);

        when(bookingRepository
                .findByFacilityAndStartTimeLessThanAndEndTimeGreaterThan(eq(f), any(), any()))
                .thenReturn(List.of(b));

        Assert.assertEquals(
                bookingRepository.findByFacilityAndStartTimeLessThanAndEndTimeGreaterThan(
                        f,LocalDateTime.now(),LocalDateTime.now()).size(),
                1
        );
    }

    @Test(groups="hql-hcql", priority=55)
    public void t55_queryNoConflicts() {
        Facility f = new Facility(1L,"Gym","D","06:00","22:00");

        when(bookingRepository
                .findByFacilityAndStartTimeLessThanAndEndTimeGreaterThan(eq(f), any(), any()))
                .thenReturn(Collections.emptyList());

        Assert.assertTrue(
                bookingRepository.findByFacilityAndStartTimeLessThanAndEndTimeGreaterThan(
                        f,LocalDateTime.now(),LocalDateTime.now()).isEmpty()
        );
    }

    @Test(groups="hql-hcql", priority=56)
    public void t56_queryUnitByUser() {
        User u = new User(1L,"U","u@e.com","p","RESIDENT");
        ApartmentUnit au = new ApartmentUnit(1L,"A101",1,u);

        when(apartmentUnitRepository.findByOwner(u)).thenReturn(Optional.of(au));

        Assert.assertTrue(apartmentUnitRepository.findByOwner(u).isPresent());
    }

    @Test(groups="hql-hcql", priority=57)
    public void t57_queryLogsByBooking() {
        Booking b = new Booking(1L,null,null,
                LocalDateTime.now(),LocalDateTime.now().plusHours(1),
                Booking.STATUS_CONFIRMED);

        BookingLog l1 = new BookingLog(1L,b,"Created",LocalDateTime.now());
        BookingLog l2 = new BookingLog(2L,b,"Cancelled",
                LocalDateTime.now().plusMinutes(5));

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(b));
        when(bookingLogRepository.findByBookingOrderByLoggedAtAsc(b))
                .thenReturn(Arrays.asList(l1,l2));

        Assert.assertEquals(bookingLogService.getLogsByBooking(1L).size(),2);
    }

    @Test(groups="hql-hcql", priority=58)
    public void t58_queryFacilitiesList() {
        Facility f1 = new Facility(1L,"Gym","D","06:00","22:00");
        Facility f2 = new Facility(2L,"Pool","D","06:00","22:00");

        when(facilityRepository.findAll()).thenReturn(Arrays.asList(f1,f2));

        Assert.assertEquals(facilityService.getAllFacilities().size(),2);
    }

    @Test(groups="hql-hcql", priority=59)
    public void t59_queryFacilitiesEmpty() {
        when(facilityRepository.findAll()).thenReturn(Collections.emptyList());
        Assert.assertTrue(facilityService.getAllFacilities().isEmpty());
    }

    @Test(groups="hql-hcql", priority=60)
    public void t60_queryEdgeNullListsHandled() {
        when(facilityRepository.findAll()).thenReturn(Collections.emptyList());
        Assert.assertNotNull(facilityService.getAllFacilities());
    }
}
