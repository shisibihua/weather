package com.honghe.weather.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 设置请求编码（utf-8）过滤器
 * @author caoqian
 * @date 20190325
 */
@WebFilter
public class EncodingFilter implements Filter {

    protected String encoding = null;

    protected FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String encoding = selectEncoding(servletRequest);
        if (encoding != null)
            servletRequest.setCharacterEncoding(encoding);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        this.encoding = null;
        this.filterConfig = null;
    }

    private String selectEncoding(ServletRequest request) {
        return (this.encoding);
    }
}
