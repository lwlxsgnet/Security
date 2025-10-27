## SpringSecurity Notes  

@author lwl  
@date 2025/10/26

### 1. 认证  
过滤器链：
- UsernamePasswordAuthenticationFilter：负责处理我们在登陆页面填写了用户名密码后的登陆请求
- ExceptionTranslationFilter：处理过滤器链中抛出的任何AccessDeniedException和AuthenticationException
- FilterSecurityInterceptor：负责权限校验的过滤器

认证流程：
- Authentication接口：它的实现类，表示当前访问系统的用户，封装了用户相关信息
- AuthenticationManager接口：定义了认证Authentication的方法
- UserDetailsService接口：加载用户特定数据的核心接口。里面定义了一个根据用户名查询用户信息的方法
- UserDetails接口：提供核心用户信息。通过UserDetailsService根据用户名获取处理的用户信息要封装成UserDetails对象返回。然后将这些信息封装到Authentication对象中