//package com.deo.activitipro.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//
//import javax.sql.DataSource;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Configuration
//public class JdbcApplicationConfiguration extends WebSecurityConfigurerAdapter {
//
//    private Logger logger = LoggerFactory.getLogger(JdbcApplicationConfiguration.class);
//    @Autowired
//    private DataSource dataSource;
//
//    @Bean
//    public UserDetailsService myUserDetailsService() {
//
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//
//        String[][] usersGroupsAndRoles = {
//                {"salaboy", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
//                {"ryandawsonuk", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
//                {"erdemedeiros", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
//                {"other", "password", "ROLE_ACTIVITI_USER", "GROUP_otherTeam"},
//                {"admin", "password", "ROLE_ACTIVITI_ADMIN"},
//        };
//
//        for (String[] user : usersGroupsAndRoles) {
//            List<String> authoritiesStrings = Arrays.asList(Arrays.copyOfRange(user, 2, user.length));
//            logger.info("> Registering new user: " + user[0] + " with the following Authorities[" + authoritiesStrings + "]");
////            jdbcUserDetailsManager.createUser(new User(user[0], passwordEncoder().encode(user[1]),
////                    authoritiesStrings.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList())));
//        }
//
//        return jdbcUserDetailsManager;
//    }
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}
