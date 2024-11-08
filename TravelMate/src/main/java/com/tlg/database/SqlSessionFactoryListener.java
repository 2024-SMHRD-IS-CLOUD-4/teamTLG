// SqlSessionFactoryListener.java
package com.tlg.database;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.ibatis.session.SqlSessionFactory;

@WebListener
public class SqlSessionFactoryListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // SqlSessionFactory를 초기화하고 ServletContext에 저장합니다.
        SqlSessionFactory sqlSessionFactory = SqlSessionManager.getSqlSessionFactory();
        sce.getServletContext().setAttribute("sqlSessionFactory", sqlSessionFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 리소스 정리가 필요하면 여기에 추가합니다.
    }
}
