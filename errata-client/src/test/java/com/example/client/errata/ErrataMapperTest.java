package com.example.client.errata;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

public class ErrataMapperTest {

    @Test
    public void testErrataUserToMap() {
        ErrataUser user = new ErrataUser();
        user.setEnabled(Boolean.TRUE);
        user.setLoginName("wguo@redhat.com");
        user.setOrganization("Engineering");
        user.setRealName("Wenjie Guo");
        user.setReceivesMail(Boolean.FALSE);
        user.setRoles(Arrays.asList("readonly", "errata"));

        Map<String, Object> mapUser = ErrataMapper.getInstance().mapMap(user);
        assertTrue(mapUser.get("enabled") instanceof Boolean);
        assertTrue(mapUser.get("roles") instanceof List);
        assertEquals(mapUser.get("login_name"), (user.getLoginName()));
        assertEquals(mapUser.get("realname"), (user.getRealName()));
    }

    @Test
    public void testMapToErrataUser() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("enabled", true);
        params.put("login_name", "wguo@redhat.com");
        params.put("roles", Arrays.asList("readonly", "errata"));

        ErrataUser user = ErrataMapper.getInstance().mapErrataUser(params);
        assertTrue(user.getEnabled());
        assertEquals(user.getRoles(), Arrays.asList("readonly", "errata"));
        assertEquals(user.getLoginName(), "wguo@redhat.com");
    }
}
