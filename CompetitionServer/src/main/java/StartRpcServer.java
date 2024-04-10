import org.example.CompetitionServerImpl;
import org.example.ICompetitionServices;
import org.example.Organizing;
import org.example.Registration;
import org.example.repository.ChildRepository;
import org.example.repository.OrganizingRepository;
import org.example.repository.RegistrationRepository;
import org.example.repository.SampleRepository;
import org.example.utils.AbstractServer;
import org.example.utils.CompetitionRpcConcurrentServer;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/competitionserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find competitionserver.properties "+e);
            return;
        }
        OrganizingRepository organizingRepo = new OrganizingRepository(serverProps);
        SampleRepository sampleRepository=new SampleRepository(serverProps);
        ChildRepository childRepository=new ChildRepository(serverProps);
        RegistrationRepository registrationRepository = new RegistrationRepository(serverProps,childRepository,sampleRepository);
        ICompetitionServices competitionServerImpl = new CompetitionServerImpl(organizingRepo, registrationRepository, childRepository,sampleRepository);
        int competitionServerPort = defaultPort;
        try{
            competitionServerPort = Integer.parseInt(serverProps.getProperty("competition.server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + competitionServerPort);
        AbstractServer server = new CompetitionRpcConcurrentServer(competitionServerPort, competitionServerImpl);
        try {
            server.start();
        } catch (org.example.utils.ServerException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                server.stop();
            } catch (org.example.utils.ServerException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
