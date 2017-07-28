package com.crossover.trial.journals.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {

    private static final long serialVersionUID = -5395369179370439193L;

    private String loginName;

    private String name;

    public UserDTO(String loginName) {
        this.loginName = loginName;
    }

    public UserDTO(String loginName, String name) {
        this.loginName = loginName;
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        if (!loginName.equals(userDTO.loginName)) return false;
        return name != null ? name.equals(userDTO.name) : userDTO.name == null;
    }

    @Override
    public int hashCode() {
        int result = loginName.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
