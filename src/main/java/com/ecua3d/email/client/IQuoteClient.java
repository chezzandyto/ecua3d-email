package com.ecua3d.email.client;

import com.ecua3d.email.config.FeignConfig;
import com.ecua3d.email.vo.QuoteToEmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "clientQuoteEmail", url = "${email.services.quote.url}", configuration = FeignConfig.class)
public interface IQuoteClient {
    @RequestMapping(method = RequestMethod.GET, value = "/quote/byId/{quoteId}")
    ResponseEntity<QuoteToEmailResponse> getByQuoteId(@PathVariable Integer quoteId);
}
