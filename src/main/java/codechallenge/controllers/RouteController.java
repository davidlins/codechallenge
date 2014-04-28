package codechallenge.controllers;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import codechallenge.services.LineService;

//TODO: Corrigir DI

@Path("route")
public class RouteController {
   
   @PersistenceContext
   EntityManager em ;
   
   @Inject
   UserTransaction utx;
   
   @GET
   @Path("/{source}/to/{target}")
   @Produces(MediaType.APPLICATION_JSON)
   public codechallenge.models.Path  getRoute(@PathParam("source") String source, @PathParam("target") String target) throws Exception{
      return new LineService(null).getShortestPath(source, target);
   }
}
