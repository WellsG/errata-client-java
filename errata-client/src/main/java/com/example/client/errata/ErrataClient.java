package com.example.client.errata;

public interface ErrataClient {

    public ErrataUser createUser(ErrataUser user) throws Exception;

    public ErrataUser updateUser(String loginName, ErrataUser user) throws Exception;

    public ErrataUser getUser(String loginName) throws Exception;

}
