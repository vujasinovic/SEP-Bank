package rs.ac.ftn.uns.sep.bank.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bank")
public class BankProperties {
    private String url;

    public BankProperties() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (url.endsWith("/"))
            url = url.substring(0, url.length() - 1); // remove / suffix

        this.url = url;
    }
}
