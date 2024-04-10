package org.example.utils;

import org.example.ICompetitionServices;
import org.example.rpcprotocol.CompetitionClientRpcWorker;

import java.net.Socket;

public class CompetitionRpcConcurrentServer extends  AbsConcurrentServer {
    private ICompetitionServices chatServer;
    public CompetitionRpcConcurrentServer(int port, ICompetitionServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        CompetitionClientRpcWorker worker=new CompetitionClientRpcWorker(chatServer, client);
        Thread tw=new Thread(worker);
        return tw;
    }
    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
