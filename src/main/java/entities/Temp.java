package entities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Temp {


        public static void main(String[] args) {

                String s = "test";
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String hashedPassword = passwordEncoder.encode(s);
                System.out.println(hashedPassword);


        }
    }
