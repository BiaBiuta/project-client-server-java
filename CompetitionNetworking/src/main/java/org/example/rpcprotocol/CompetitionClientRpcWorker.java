package org.example.rpcprotocol;

import org.example.*;
import org.example.dto.*;
import org.example.utils.ServerException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class CompetitionClientRpcWorker implements Runnable, ICompetitionObserver {
    private ICompetitionServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public CompetitionClientRpcWorker(ICompetitionServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                System.out.println("Request received "+request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (CompetitionException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }
    public void organizingLoggedIn(Organizing user) throws CompetitionException {
        System.out.println("Organizing logged in "+user);
        Response response=new Response.Builder().type(ResponseType.ORGANIZING_LOGGED_IN).build();
        try {
            sendResponse(response);
        } catch (IOException e) {
            throw new CompetitionException("Sending error: "+e);
        }
    }
    public void organizingLoggedOut(Organizing user) throws CompetitionException {
        System.out.println("Organizing logged out "+user);
        Response response=new Response.Builder().type(ResponseType.ORGANIZING_LOGGED_OUT).build();
        try {
            sendResponse(response);
        } catch (IOException e) {
            throw new CompetitionException("Sending error: "+e);
        }
    }
    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();
    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
    private Response handleRequest(Request request) throws CompetitionException {
        Response response=null;
        if (request.type()== RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());
            OrganizingDTO oDto=(OrganizingDTO) request.data();
//            Organizing org = DTOUtils.getFromDTO(oDto);
            Organizing org= server.findOrganizing(oDto.getUsername(),oDto.getPassword());
            try {
                server.login(org,this);
                return okResponse;
            } catch (CompetitionException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGOUT){
            System.out.println("Logout request");
            // LogoutRequest logReq=(LogoutRequest)request;
            OrganizingDTO udto=(OrganizingDTO) request.data();
            Organizing user= DTOUtils.getFromDTO(udto);
            try {
                server.logout(user,this);
                connected=false;
                return okResponse;

            } catch (CompetitionException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.FIND_ALL_SAMPLES){
            System.out.println("FindAllSamples ...");
            Iterable<Sample> samples=server.findAllSamples();

            List<SamplesDTO> samplesDTO= DTOUtils.getDTOSample(samples);

            return new Response.Builder().type(ResponseType.FIND_ALL_SAMPLES).data(samplesDTO).build();
        }
        if (request.type()== RequestType.FIND_CHILD){
            System.out.println("GetLoggedFriends Request ...");
            ChildDTO udto=(ChildDTO) request.data();
            Child user= server.findChild(udto.getName());
            return new Response.Builder().type(ResponseType.FIND_CHILD).data(user).build();
        }
        if (request.type()== RequestType.SAVE_CHILD){
            System.out.println("GetLoggedFriends Request ...");
            ChildDTO udto=(ChildDTO) request.data();
            Child user= server.saveChild(udto.getName(),Integer.parseInt(udto.getAge()));
            return new Response.Builder().type(ResponseType.SAVE_CHILD).data(DTOUtils.getDTO(user)).build();
        }

        if (request.type()== RequestType.FIND_ORGANIZING){
            System.out.println("GetLoggedFriends Request ...");
            OrganizingDTO udto=(OrganizingDTO) request.data();
            Organizing user= DTOUtils.getFromDTO(udto);
            return new Response.Builder().type(ResponseType.FIND_ORGANIZING).data(user).build();
        }
        if (request.type()== RequestType.FIND_SAMPLE){
            System.out.println("FindSample Request ...");
            SamplesDTO sdto=(SamplesDTO) request.data();
            Sample sam=DTOUtils.getFromDTO(sdto);
            Sample sample=server.findSample(sdto.getAgeCategory(),sdto.getSampleCategory());
            return new Response.Builder().type(ResponseType.FIND_SAMPLE).data(DTOUtils.getDTO(sample)).build();
        }
        if (request.type()== RequestType.REGISTER_CHILD){
            System.out.println("RegisterChild Request ...");
            RegistrationDTO rdto=(RegistrationDTO) request.data();
            Registration reg=DTOUtils.getFromDTO(rdto);
            Registration registration=server.registerChild(reg.getChild(),reg.getSample());
            return new Response.Builder().type(ResponseType.PARTICIPANTS_REGISTERED).data(DTOUtils.getDTO(registration)).build();
        }
        if (request.type()== RequestType.LIST_CHILDREN_FOR_SAMPLE){
            System.out.println("GetParticipants Request ...");
            SamplesDTO sdto=(SamplesDTO) request.data();
            Sample sample=server.findSample(sdto.getAgeCategory(),sdto.getSampleCategory());
            List<Child> participants=server.listChildrenForSample(sample);
            return new Response.Builder().type(ResponseType.LIST_CHILDREN).data(DTOUtils.getDTOChild(participants)).build();
        }

        return response;
    }

    @Override
    public void participantsRegistered(Registration org) throws CompetitionException {
        System.out.println("Participants registered "+org);
        RegistrationDTO orgDTO= DTOUtils.getDTO(org);
        Response response=new Response.Builder().type(ResponseType.PARTICIPANTS_REGISTERED).data(orgDTO).build();
        try {
            sendResponse(response);
        } catch (IOException e) {
            throw new CompetitionException("Sending error: "+e);
        }
    }
}
