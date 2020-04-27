package web.listener;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import dao.FriendRequestDao;
import dao.MessageDao;
import dao.UserDao;
import dao.impl.PgFriendRequestDao;
import dao.impl.PgMessageDao;
import dao.impl.PgUserDao;
import db.PgConfig;
import db.PgConfigProvider;
import db.PgDataSourceProvider;
import service.FriendService;
import service.MessageService;
import service.SecurityService;
import service.UserService;
import service.impl.FriendServiceImpl;
import service.impl.MessageServiceImpl;
import service.impl.SecurityServiceImpl;
import service.impl.UserServiceImpl;
import web.filter.LoggedInFilter;
import web.filter.RequestEscapingFilter;
import web.filter.ResponseAddHeadersFilter;
import web.servlet.*;

import javax.inject.Singleton;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@WebListener
public class GuiceConfig extends GuiceServletContextListener {

    private static class PgDbModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(PgConfig.class).toProvider(PgConfigProvider.class).in(Singleton.class); //создать провайдера и вызвать его метод get
            bind(DataSource.class).toProvider(PgDataSourceProvider.class).in(Singleton.class);
        }
    }

    private static class DependencyModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(UserDao.class).to(PgUserDao.class).in(Singleton.class);
            bind(MessageDao.class).to(PgMessageDao.class).in(Singleton.class);
            bind(FriendRequestDao.class).to(PgFriendRequestDao.class).in(Singleton.class);
            bind(SecurityService.class).to(SecurityServiceImpl.class).in(Singleton.class);
            bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);
            bind(MessageService.class).to(MessageServiceImpl.class).in(Singleton.class);
            bind(FriendService.class).to(FriendServiceImpl.class).in(Singleton.class);
        }
    }

    private static class ServletConfigModule extends ServletModule {
        @Override
        protected void configureServlets() {
            Map<String, String> loggedInFilterParams = new HashMap<>();
            loggedInFilterParams.put("sessionAttributeName", "user");
            loggedInFilterParams.put("allowedUnauth", "/locale,/login,/registration");
            loggedInFilterParams.put("forbiddenUnauth", null);
            loggedInFilterParams.put("redirectUnauth", "/login");
            loggedInFilterParams.put("allowedAuth", null);
            loggedInFilterParams.put("forbiddenAuth", "/login,/registration");
            loggedInFilterParams.put("redirectAuth", "/profile");

            filter("/*").through(RequestEscapingFilter.class);
            filter("/*").through(ResponseAddHeadersFilter.class);
            filter("/*").through(LoggedInFilter.class, loggedInFilterParams);

            serve("/locale").with(LocaleServlet.class);
            serve("/login").with(LogInServlet.class);
            serve("/logout").with(LogOutServlet.class);
            serve("/registration").with(RegistrationServlet.class);
            serve("/profile").with(ProfileServlet.class);
            serve("/profileChange").with(ProfileChangeServlet.class);
            serve("/friends").with(FriendsServlet.class);
            serve("/messages").with(MessagesServlet.class);
            serve("/search").with(SearchServlet.class);
        }
    }

    @Override
    protected Injector getInjector() {

        return Guice.createInjector(
                new PgDbModule(),
                new DependencyModule(),
                new ServletConfigModule()
        );
    }
}
