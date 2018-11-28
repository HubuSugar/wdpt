package edu.hubu.wdpt.dao;

import edu.hubu.wdpt.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * created by Sugar  2018/11/11 12:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserDAO {

    @Autowired
    UserDAO userDAO;

    @Test
    public void testaddUser(){
        User user = new User();
        user.setName("章泽天");
        user.setPassword("12346");
        user.setSalt("dkadkadhai");
        user.setHeadUrl("http://image/1.jpg");
        int k =  userDAO.addUser(user);
        System.out.println(k + "===============================");
    }

    @Test
    public void testselectById(){
        User user = userDAO.selectById(7);
        System.out.println(user+"===============================");
    }

    @Test
    public void testselectByName(){
        User user = userDAO.selectByName("章泽天");
        System.out.println(user+"===============================");
    }

    @Test
    public void testUpdateUserPass(){
        User user = new User();
        user.setId(7);
        user.setName("章泽天");
        user.setPassword("123456");
        user.setSalt("dkadkadhai");
        user.setHeadUrl("http://image/1.jpg");
        userDAO.updatePassword(user);
    }

}
