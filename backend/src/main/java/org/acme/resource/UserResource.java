package org.acme.resource;

import java.util.HashMap;
import java.util.Map;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestQuery;
import org.acme.entity.User;
import org.acme.entity.UserEntity;
import org.acme.repository.UserRepository;
import org.acme.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/users")
public class UserResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(Map<String, Object> requestBody) {
        String name = (String) requestBody.get("name");  // JSONからnameを取得
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Name is required");
        }

        UserEntity user = UserService.createUser(name);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.id);
        userMap.put("name", user.name);

        return Response.status(Response.Status.CREATED).entity(userMap).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id) {
        UserEntity user = UserService.getUserById(id);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.id);
        userMap.put("name", user.name);

        return Response.ok(userMap).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserName(@PathParam("id") Long id, Map<String, Object> requestBody) {
        String name = (String) requestBody.get("name");  // JSONからnameを取得
        if (name == null) {
            throw new BadRequestException("Name is required");
        }

        UserEntity user = UserService.updateUserName(id, name);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.id);
        userMap.put("name", user.name);

        return Response.ok(userMap).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Long id) {
        UserService.deleteUser(id);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", "User deleted successfully");
        responseMap.put("id", id);

        return Response.ok(responseMap).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUsers(
            @QueryParam("id") Long id,
            @QueryParam("name") String name,
            @QueryParam("_page") Integer page,
            @QueryParam("_limit") Integer limit) {
                List<UserRepository.UserDTO> userDTOs = UserService.listUsers(id, name, page, limit);
    
                return Response.ok(userDTOs).build();
        }
    } 
