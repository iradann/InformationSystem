package com.taskadapter.redmineapi.bean;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.Collection;

public class UserTest {
    @Test
    public void customFieldWithDuplicateIDReplacesTheOldOne() {
        User user = UserFactory.create();
        CustomField field = CustomFieldFactory.create(5, "name1", "value1");
        CustomField duplicateField = CustomFieldFactory.create(5, "name1", "value1");
        assertThat(user.getCustomFields().size()).isEqualTo(0);
        user.addCustomField(field);
        user.addCustomField(duplicateField);
        assertThat(user.getCustomFields().size()).isEqualTo(1);
    }

    @Test
    public void usersWithEqualIDAreEqual() {
        User user1 = UserFactory.create(10);
        user1.setLogin("login1");
        User user2 = UserFactory.create(10);
        user2.setLogin("login2");
        assertThat(user1).isEqualTo(user2);
    }

    @Test
    public void usersWithDifferentIdsAreNotEqual() {
        User user1 = UserFactory.create(1);
        user1.setLogin("login");
        user1.setFirstName("first");
        user1.setLastName("last");
        User user2 = UserFactory.create(2);
        user2.setLogin("login1");
        user2.setFirstName("first");
        user2.setLastName("last");
        assertThat(user1).isNotEqualTo(user2);
    }
}
