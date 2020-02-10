package ru.TodoManager;

import org.testng.annotations.DataProvider;

public class InterviewTaskData {

    @DataProvider(name = "credoLogin")
    public static Object[][] credoLogin() {
        return new Object[][]{
                {"simon_dow@some.domaine.com", "123456789"}
        };
    }

    @DataProvider(name = "getGoodCredentials")
    public static Object[][] getGoodCredentials() {
        return new Object[][]{
                {"john_dow@some.domaine.com", "123456789"},
                {"simon_dow@some.domaine.com", "123456789"},
                {"JOHN_DOW@SOME.DOMAIN.COM", "123456789"},
                {"SIMON_DOW@SOME.DOMAIN.COM", "123456789"},
        };
    }

    @DataProvider(name = "getBadCredentials")
    public static Object[][] getBadCredentials() {
        return new Object[][]{
                {"<script>alert(123)</script>", "123456789"},
                {" ", " "},
                {"", ""},
                {"john_dow@some.domaine.com", "1234567"},
                {"simon_dow@some.domaine.com", "34534"},
                {"sdnkd", "раплыформ"},
                {"(‘ or ‘a’ = ‘a’; DROP TABLE user; SELECT * FROM todo;)", "123456789"},
                {" «♣☺♂» , «»‘~!@#$%^&*()?>,./\\<][ /*<!—«», «${code}»;—>", " «♣☺♂» , «»‘~!@#$%^&*()?>,./\\<][ /*<!—«», «${code}»;—>"},
                {"                  ", "123456789"},
                {"      john_dow@some.domaine.com", "123456789"},
                {"john_dow@some.domaine.com     ", "123456789"},
                {"ksjdnfjdhfgbsjhgblsjbvlakjebgiejrngbkdjsfnbjdfnblejgbsdljgbldjsfb отвир дои лфодукипдофурипдфлоукипдофвиапдофрваидофримдфоуипмуолфип млфоидфуои", "djfghjeskhgbkehblvjWHBFVLAHUEBDGLHUHERLGBLJAERFBLJAEHBLJADRHBGLAJGHB;JRGNBKAJBFLJBVLSJFDBAKJEBNFKJRBGLJSEBRGLKJEBRGKJLBERGJBVSJDFBGVJAEBJAEBRGAKEJB"}


        };
    }
}
