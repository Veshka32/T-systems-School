FROM jboss/wildfly
ADD standalone-full.xml /opt/jboss/wildfly/standalone/configuration/

ADD mobile.war /opt/jboss/wildfly/standalone/deployments/

#add user admin with password admin to use adminconsole on port 9990
RUN /opt/jboss/wildfly/bin/add-user.sh admin admin#70365 --silent

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0","-c","standalone-full.xml"]
