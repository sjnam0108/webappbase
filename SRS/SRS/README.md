 ###version
```
STS 3.9.4
tomcat 8.5
JDK 1.8
```
--------------------
 ###used
- bootstrap4
- kendoUI
    - grid
    
--------------------
 ###src structure
- hibernate.cfg.xml

> kr.co.bbmc.dbsetup
> > Creation

> > initData

> WEB-INF
> > cashgo-servlet.xml

> servers8.5
> > server.xml
> > > cashgopath

> uploadtemp 폴더가 필요

> http://localhost:8080/bbmc-cashgo/

> <Context docBase="CashGo" path="/" reloadable="true" source="org.eclipse.jst.jee.server:CashGo"/></Host> 
> > Plz put the path in /

> LoanInterest.java
> > 계산식 변경

------------------------
 ####reference
> [reference1](https://argali.tistory.com/20)<br>
> [reference2](https://m.blog.naver.com/PostView.nhn?blogId=keiaz&logNo=220828317924&proxyReferer=https:%2F%2Fwww.google.com%2F)<br>
> [reference3](https://tnsgud.tistory.com/542)
