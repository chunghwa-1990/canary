# canary

- springboot 3.0
- jwt token 4.3
- redis 7.0
- mybatis plus 3.5.3.1
- mybatis 3.0
- [mysql 集群](mysql.md)
- [proxysql](proxysql.md)
- 一主二从高可用（mha）
说明：读写分离和高可用选择一种方案，local环境为高可用环境，其他为读写分离环境

#### 编译
- maven
```powershell
$ mvn clean install -Dmaven.test.skip=true
```
- maven-mind （推荐）
```powershell
$ mvnd clean install -Dmaven.test.skip=true 
```
#### 优雅停服
- httpie
```powershell
$ http post http://localhost:8080/actuator/shutdown
```
- curl
```powershell
$ curl -X post http://localhost:8080/actuator/shutdown
```