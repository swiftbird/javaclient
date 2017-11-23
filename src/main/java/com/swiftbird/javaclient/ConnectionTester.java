package com.swiftbird.javaclient;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.organizations.GetOrganizationRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsResponse;
import org.cloudfoundry.client.v2.organizations.OrganizationEntity;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.operations.organizations.OrganizationSummary;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.util.ResourceUtils;

import reactor.core.publisher.Mono;

public class ConnectionTester {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		
				
		DefaultConnectionContext connectionContext = DefaultConnectionContext.builder()
	    .apiHost("api.run.pivotal.io")
	    .build();
		
		PasswordGrantTokenProvider tokenProvider = PasswordGrantTokenProvider.builder()
	    .password("Gand4alf")
	    .username("sanderson@pivotal.io")
	    .build();
		
		ReactorCloudFoundryClient client = ReactorCloudFoundryClient.builder()
	    .connectionContext(connectionContext)
	    .tokenProvider(tokenProvider)
	    .build();
		
		ReactorDopplerClient dopplerClient = ReactorDopplerClient.builder()
	    .connectionContext(connectionContext)
	    .tokenProvider(tokenProvider)
	    .build();

		ReactorUaaClient uaaClient = ReactorUaaClient.builder()
	    .connectionContext(connectionContext)
	    .tokenProvider(tokenProvider)
	    .build();
		
		System.out.println(" +++  Applications: " + client.applicationsV2());
		 
		DefaultCloudFoundryOperations cfo = DefaultCloudFoundryOperations.builder()
	    .cloudFoundryClient(client)
	    .dopplerClient(dopplerClient)
	    .uaaClient(uaaClient)
	    .organization("pivot-sanderson")
	    .space("development")
	    .build();
		
		System.out.println("CF Operations");
		cfo.organizations()
	    .list()
	    .map(OrganizationSummary::getName)
	    .subscribe(System.out::println);
		
		Mono<OrganizationEntity> orgent = client.organizations().get(GetOrganizationRequest.builder().organizationId("pivot-sanderson").build()).map(ResourceUtils::getEntity);
		
		orgent.log();
		
		
		
	}

}
