package com.example.client.errata;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ErrataRPCCallTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrataRPCCallTest.class);

    private ErrataClient errataClient;

    @BeforeMethod
    public void setUpHandler() {
        //TODO: Server URL 
        errataClient = ErrataClientImpl.create("");
    }

    @Test
    public void testGetUser() throws Exception {
        ErrataUser user = errataClient.getUser("login_name");
        assertNotNull(user);
        assertEquals(user.getLoginName(), "login_name");
        assertTrue(user.getEnabled());
        assertTrue(user.getRoles().contains("errata"));
    }

    @Test
    public void testCreateUser() throws Exception {
        ErrataUser user = new ErrataUser();
        user.setLoginName("login_name");
        user.setRealName("Real Name");
        errataClient.createUser(user);
    }

    @Test
    public void testUpdateUser() throws Exception {
        ErrataUser user = new ErrataUser();
        user.setEnabled(Boolean.FALSE);
        ErrataUser errataUser = errataClient.updateUser("login_name", user);
        assertFalse(errataUser.getEnabled());
    }
}
