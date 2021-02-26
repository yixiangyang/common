package com.xiangyang.http;

import lombok.Builder;
import lombok.Data;

@Data
public class User {
    private String name;

    private String pwd;


    public static final class UserBuilder {
        private String name;
        private String pwd;

        private UserBuilder() {
        }

        public static UserBuilder anUser() {
            return new UserBuilder();
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder pwd(String pwd) {
            this.pwd = pwd;
            return this;
        }

        public User build() {
            User user = new User();
            user.setName(name);
            user.setPwd(pwd);
            return user;
        }
    }
}
