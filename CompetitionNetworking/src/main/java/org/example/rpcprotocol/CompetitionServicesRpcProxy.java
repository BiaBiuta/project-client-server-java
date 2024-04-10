package org.example.rpcprotocol;

import org.example.*;
import org.example.dto.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CompetitionServicesRpcProxy implements ICompetitionServices {
    private String host;
    private int port;

    private ICompetitionObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public CompetitionServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;

        qresponses=new LinkedBlockingQueue<Response>();
    }

    public ICompetitionObserver getClient() {
        return client;
    }

    public void setClient(ICompetitionObserver client) {
        this.client = client;
    }

    @Override
    public Child findChild(String name) throws CompetitionException {
        ChildDTO org=new ChildDTO(name);
        Request req=new Request.Builder().type(RequestType.FIND_CHILD).data(org).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new CompetitionException(err);
        }
        ChildDTO orgDTO= (ChildDTO) response.data();
        if (orgDTO==null)
            return null;
        Child org1= DTOUtils.getFromDTO(orgDTO);
        return org1;
    }

    @Override
    public Organizing login(Organizing org,ICompetitionObserver observer) throws CompetitionException {
        initializeConnection();
        OrganizingDTO udto= DTOUtils.getDTO(org);
        Request req=new Request.Builder().type(RequestType.LOGIN).data(udto).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            this.client=observer;
            return DTOUtils.getFromDTO((OrganizingDTO)response.data());
        }
        else {
            String err=response.data().toString();
            closeConnection();
            throw new CompetitionException(err);
        }
    }

    @Override
    public void logout(Organizing user,ICompetitionObserver observer) throws CompetitionException {
        OrganizingDTO udto= DTOUtils.getDTO(user);
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(udto).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new CompetitionException(err);
        }
    }

    @Override
    public Organizing findOrganizing(String username, String password) throws CompetitionException {
        OrganizingDTO org=new OrganizingDTO(username,password);
        Request req=new Request.Builder().type(RequestType.FIND_ORGANIZING).data(org).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new CompetitionException(err);
        }
        OrganizingDTO orgDTO= (OrganizingDTO) response.data();
        Organizing org1= DTOUtils.getFromDTO(orgDTO);
        return org1;
    }

    @Override
    public int numberOfChildrenForSample(Sample sample) {
        return 0;
    }

    @Override
    public Iterable<Sample> findAllSamples() throws CompetitionException {
        Request req=new Request.Builder().type(RequestType.FIND_ALL_SAMPLES).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new CompetitionException(err);
        }
        List<SamplesDTO> samplesDTO= (List<SamplesDTO>) response.data();
        List<Sample> samples= DTOUtils.getFromDTOSamples(samplesDTO);
        return samples;
    }

    @Override
    public Child saveChild(String name, int age) throws CompetitionException {
        Child child=new Child(name,age);
        ChildDTO childDTO=new ChildDTO(name,String.valueOf(age));
        Request req=new Request.Builder().type(RequestType.SAVE_CHILD).data(childDTO).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new CompetitionException(err);
        }
        ChildDTO childDTO1= (ChildDTO) response.data();
        Child child1 = DTOUtils.getFromDTO(childDTO1);
        return child1;
    }

    @Override
    public Sample findSample(String ageCategory, String desen) throws CompetitionException {
        SamplesDTO sampleDTO=new SamplesDTO(desen,ageCategory);
        //Sample sample= DTOUtils.getFromDTO(sampleDTO);
        sendRequest(new Request.Builder().type(RequestType.FIND_SAMPLE).data(sampleDTO).build());
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new CompetitionException(err);
        }
        SamplesDTO sampleDTO1= (SamplesDTO) response.data();
        Sample sample1= DTOUtils.getFromDTO(sampleDTO1);
        System.out.println(sample1.getId());
        return sample1;
    }

    @Override
    public Registration registerChild(Child child, Sample sample) throws CompetitionException {
        ChildDTO childDTO= DTOUtils.getDTO(child);
        Child child1= findChild(childDTO.getName());
        if(child1==null){
            child1=saveChild(child.getName(),child.getAge());
        }
        SamplesDTO sampleDTO= DTOUtils.getDTO(sample);
        Sample sample1= findSample(sampleDTO.getAgeCategory(),sampleDTO.getSampleCategory());
        RegistrationDTO regDTO=new RegistrationDTO(child1.getId().toString(),sample1.getId().toString());
        Request req=new Request.Builder().type(RequestType.REGISTER_CHILD).data(regDTO).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new CompetitionException(err);
        }
        RegistrationDTO regDTO1= (RegistrationDTO) response.data();
        Registration reg= DTOUtils.getFromDTO(regDTO1);
        return reg;
    }

    @Override
    public List<Child> listChildrenForSample(Sample sample) throws CompetitionException {
        Request req = new Request.Builder().type(RequestType.LIST_CHILDREN_FOR_SAMPLE).data(DTOUtils.getDTO(sample)).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new CompetitionException(err);
        }
        List<ChildDTO> childDTO = (List<ChildDTO>) response.data();
        List<Child> children = DTOUtils.getFromDTOChild(childDTO);
        return children;

    }
    private void initializeConnection() throws CompetitionException {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }
    private void sendRequest(Request request)throws CompetitionException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new CompetitionException("Error sending object "+e);
        }

    }
    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private boolean isUpdate(Response response){
        return response.type()== ResponseType.PARTICIPANTS_REGISTERED ;
    }
    private void handleUpdate(Response response){
        if (response.type()== ResponseType.PARTICIPANTS_REGISTERED){
            RegistrationDTO regDTO= (RegistrationDTO) response.data();
            Registration org= DTOUtils.getFromDTO((regDTO));

            try {
                client.participantsRegistered(org);
            } catch (CompetitionException e) {
                e.printStackTrace();
            }
        }

    }
    private Response readResponse() throws CompetitionException {
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
