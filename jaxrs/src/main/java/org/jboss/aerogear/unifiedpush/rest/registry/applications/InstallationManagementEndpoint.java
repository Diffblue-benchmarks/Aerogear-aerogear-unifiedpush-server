/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.unifiedpush.rest.registry.applications;

import org.jboss.aerogear.unifiedpush.api.Installation;
import org.jboss.aerogear.unifiedpush.api.Variant;
import org.jboss.aerogear.unifiedpush.service.ClientInstallationService;
import org.jboss.aerogear.unifiedpush.service.GenericVariantService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Stateless
@Path("/applications/{variantID}/installations/")
public class InstallationManagementEndpoint {

    @Inject
    private GenericVariantService genericVariantService;

    @Inject
    private ClientInstallationService clientInstallationService;

    @Context
    protected SecurityContext sec;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findInstallations(@PathParam("variantID") String variantId) {

        //Find the variant using the variantID
        Variant variant = genericVariantService.findByVariantIDForDeveloper(variantId, sec.getUserPrincipal().getName());

        if (variant == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Could not find requested Variant").build();
        }

        return Response.ok(variant.getInstallations()).build();
    }

    @GET
    @Path("/{installationID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findInstallation(@PathParam("variantID") String variantId, @PathParam("installationID") String installationId) {

        Installation installation = clientInstallationService.findById(installationId);

        if (installation == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Could not find requested Installation").build();
        }

        return Response.ok(installation).build();
    }

    @PUT
    @Path("/{installationID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateInstallation(Installation entity, @PathParam("variantID") String variantId, @PathParam("installationID") String installationId) {

        Installation installation = clientInstallationService.findById(installationId);

        if (installation == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Could not find requested Installation").build();
        }

        clientInstallationService.updateInstallation(installation, entity);

        return Response.noContent().build();

    }

    @DELETE
    @Path("/{installationID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeInstallation(@PathParam("variantID") String variantId, @PathParam("installationID") String installationId) {

        Installation installation = clientInstallationService.findById(installationId);

        if (installation == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Could not find requested Installation").build();
        }

        // remove it
        clientInstallationService.removeInstallation(installation);

        return Response.noContent().build();
    }
}
