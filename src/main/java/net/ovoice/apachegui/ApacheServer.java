package net.ovoice.apachegui;

/**
 * Created by peter on 29.07.16.
 */
public class ApacheServer {

    public String name;
    private AG_Runtime agRuntime;
    private String[] lastCmd;

    public ApacheServer() {
        agRuntime = new AG_Runtime();
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public String restart() {
        String[] agCmd = {"service", "apache2", "restart"};
        lastCmd = agCmd;
        return agRuntime.exec(agCmd);
    }

    public String status() {
        String[] agCmd = {"service", "apache2", "status"};
        lastCmd = agCmd;
        return agRuntime.exec(agCmd);
    }
}
