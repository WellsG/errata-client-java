package com.example.client.errata;

import java.io.Serializable;
import java.util.List;

public class ErrataUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String loginName;

    private String organization;

    private String realName;

    private Boolean receivesMail;

    private Boolean enabled;

    private List<String> roles;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Boolean getReceivesMail() {
        return receivesMail;
    }

    public void setReceivesMail(Boolean receivesMail) {
        this.receivesMail = receivesMail;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
        result = prime * result + ((loginName == null) ? 0 : loginName.hashCode());
        result = prime * result + ((organization == null) ? 0 : organization.hashCode());
        result = prime * result + ((realName == null) ? 0 : realName.hashCode());
        result = prime * result + ((receivesMail == null) ? 0 : receivesMail.hashCode());
        result = prime * result + ((roles == null) ? 0 : roles.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ErrataUser other = (ErrataUser) obj;
        if (enabled == null) {
            if (other.enabled != null)
                return false;
        } else if (!enabled.equals(other.enabled))
            return false;
        if (loginName == null) {
            if (other.loginName != null)
                return false;
        } else if (!loginName.equals(other.loginName))
            return false;
        if (organization == null) {
            if (other.organization != null)
                return false;
        } else if (!organization.equals(other.organization))
            return false;
        if (realName == null) {
            if (other.realName != null)
                return false;
        } else if (!realName.equals(other.realName))
            return false;
        if (receivesMail == null) {
            if (other.receivesMail != null)
                return false;
        } else if (!receivesMail.equals(other.receivesMail))
            return false;
        if (roles == null) {
            if (other.roles != null)
                return false;
        } else if (!roles.equals(other.roles))
            return false;
        return true;
    }

}
