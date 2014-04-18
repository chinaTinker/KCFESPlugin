package com.kcf.test;

import com.kcf.entity.Book;
import com.kcf.entity.User;
import com.kcf.repo.BookRepo;
import com.kcf.repo.DiscussRepo;
import com.kcf.repo.HospitalRepo;
import com.kcf.repo.UserRepo;
import com.kcf.util.DBHelper;
import com.kcf.util.RiverConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * User: 老牛 -- TK
 * Date: 14-4-1
 * Time: 下午5:02
 */
@RunWith(JUnit4.class)
public class TestRepo {
    @Before
    public void init(){
        DBHelper.init(
            RiverConfig.DEFAULT_DB_URL,
            RiverConfig.DEFAULT_DB_USER,
            RiverConfig.DEFAULT_DB_PWD
        );
    }

    private void testBookRepo() {
        System.out.println("test bookRepo");

        BookRepo bookRepo = new BookRepo();

        Book book = bookRepo.getBookById(1l);

        assert  book != null;
        assert  book.getId() == 1l;
        assert  book.getOutline() != null && book.getOutline().contains("甘油三酯");
        assert  book.getSlug().equals("226180");
        assert  book.getStory() == null;
        assert  book.getQuestion() == null || "".equals(book.getQuestion());

    }

    private void testDiscussRepo() {
        System.out.println("test DiscussRepo");

        DiscussRepo discussRepo = new DiscussRepo();
        String replies = discussRepo.getReplies(18l);


        assert replies != null;
        assert !replies.equals("");

        String[] contents = replies.split("#");

        assert contents != null;
        assert contents.length == 3;

    }

    private void testHospitalRepo(){
        System.out.println("test hospitalRepo");

        HospitalRepo hospitalRepo = new HospitalRepo();
        String hospitalsStr = hospitalRepo.getHospitals(1l);

        assert hospitalsStr != null;

        String[] hospitals = hospitalsStr.split(",");

        assert hospitals.length > 0;

    }

    private void testUserRepo(){
        System.out.println("test userRepo");

        UserRepo userRepo = new UserRepo();

        User user = userRepo.getUserById(1l);

        assert user != null;
        assert user.getId() == 1l;
        assert user.getName().equals("马丁医生");
        assert user.getAge() == 2014 - 1975;
        assert user.getGender().equals("男");
    }

    @Test
    public void doTest(){
        this.testBookRepo();
        this.testDiscussRepo();
        this.testHospitalRepo();
        this.testUserRepo();
    }
}
