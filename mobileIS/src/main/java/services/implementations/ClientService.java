package services.implementations;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dao.interfaces.ClientDaoI;
import dao.interfaces.ContractDaoI;
import model.dto.ClientDTO;
import model.entity.Client;
import model.entity.Contract;
import model.helpers.PaginateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import services.exceptions.ServiceException;
import services.interfaces.ClientServiceI;

import java.util.List;

@Service
@EnableTransactionManagement
@Transactional
public class ClientService implements ClientServiceI {
    private ClientDaoI clientDAO;
    private ContractDaoI contractDaoI;

    @Autowired
    public void setDAO(ClientDaoI clientDAO, ContractDaoI contractDaoI) {
        this.clientDAO = clientDAO;
        this.clientDAO.setClass(Client.class);
        this.contractDaoI = contractDaoI;
        contractDaoI.setClass(Contract.class);
    }

    @Override
    public String findByPassport(String passport) {
        Gson gson = new Gson();
        JsonElement element = new JsonObject();

        if (!passport.matches("^[0-9]{10}")) {
            element.getAsJsonObject().addProperty("status", "error");
            element.getAsJsonObject().addProperty("message", "must be 10 digits");
        } else {
            Client client = clientDAO.findByPassportId(passport);
            if (client == null) {
                element.getAsJsonObject().addProperty("status", "error");
                element.getAsJsonObject().addProperty("message", "there is no such client");
            } else {
                ClientDTO dto = new ClientDTO(client);
                element.getAsJsonObject().addProperty("status", "success");
                element.getAsJsonObject().add("client", gson.toJsonTree(dto));
            }
        }
        return gson.toJson(element);
    }

    @Override
    public void create(ClientDTO dto) throws ServiceException {
        if (clientDAO.isPassportExist(dto.getPassportId()))
            throw new ServiceException("passportId is reserved");

        if (clientDAO.isEmailExists(dto.getEmail()))
            throw new ServiceException("email is reserved");

        Client client=new Client();
        updateFields(client,dto);
        clientDAO.save(client);
        dto.setId(client.getId());
    }

    @Override
    public void update(ClientDTO dto) throws ServiceException{
        Client client = clientDAO.findByPassportId(dto.getPassportId());
        if (client != null && client.getId() != dto.getId())  //check if passportId is unique
            throw new ServiceException("passportId is reserved");

        client = clientDAO.findByEmail(dto.getEmail());
        if (client != null && client.getId() != dto.getId())  //check if email is unique
            throw new ServiceException("email is reserved");

        client=clientDAO.findOne(dto.getId());
        updateFields(client,dto);
        clientDAO.update(client);
    }

    @Override
    public void delete(int id){
        clientDAO.deleteById(id);
    }

    @Override
    public ClientDTO getDTO(int id){
        ClientDTO dto = new ClientDTO(clientDAO.findOne(id));
        dto.setContractsList(contractDaoI.getClientContracts(id));
        return dto;
    }

    @Override
    public PaginateHelper<Client> getPaginateData(Integer currentPage, int rowPerPage) {

        if (currentPage == null) currentPage = 1;  //if no page specified, show first page
        if (currentPage < 1 || rowPerPage < 0) throw new IllegalArgumentException();
        List<Client> optionsForPage = clientDAO.allInRange((currentPage - 1) * rowPerPage, rowPerPage);
        int total = clientDAO.count().intValue();
        int totalPage = total / rowPerPage;
        if (total % rowPerPage > 0) totalPage++;
        return new PaginateHelper<>(optionsForPage, totalPage);
    }

    private void updateFields(Client client, ClientDTO dto){
        client.setAddress(dto.getAddress());
        client.setEmail(dto.getEmail());
        client.setName(dto.getName());
        client.setSurname(dto.getSurname());
        client.setPassportId(dto.getPassportId());
        client.setBirthday(dto.getBirthday());
    }
}
