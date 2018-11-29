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
import services.interfaces.ClientServiceI;

import java.util.List;
import java.util.Optional;

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
    public String getJsonByPassport(String passport) {
        Gson gson = new Gson();
        JsonElement element = new JsonObject();
        try {
            ClientDTO dto = getByPassport(passport);
            if (dto == null) {
                setError(element, "there is no such client");
            } else {
                setsucces(element, gson.toJsonTree(dto));
            }
        } catch (NumberFormatException e) {
            setError(element, "must be 10 digits");
        }
        return gson.toJson(element);
    }

    @Override
    public ClientDTO getByPassport(String passport) {
        if (!passport.matches("^[0-9]{10}")) throw new NumberFormatException();
        Client client = clientDAO.findByPassportId(passport);
        if (client == null) return null;
        else return new ClientDTO(client);
    }

    @Override
    public String getJsonByPhone(String phone) {
        Gson gson = new Gson();
        JsonElement element = new JsonObject();
        try {
            ClientDTO dto = getByPhone(phone);
            if (dto == null) {
                setError(element, "there is no such client");
            } else {
                setsucces(element, gson.toJsonTree(dto));
            }
        } catch (NumberFormatException e) {
            setError(element, "must be 10 digits");
        }
        return gson.toJson(element);
    }

    private void setError(JsonElement element, String message) {
        element.getAsJsonObject().addProperty("status", "error");
        element.getAsJsonObject().addProperty("message", message);
    }

    private void setsucces(JsonElement element, JsonElement tree) {
        element.getAsJsonObject().addProperty("status", "success");
        element.getAsJsonObject().add("client", tree);
    }

    @Override
    public ClientDTO getByPhone(String phone) {
        if (!phone.matches("^[0-9]{10}")) throw new NumberFormatException();
        Client client = contractDaoI.findClientByPhone(Long.parseLong(phone));
        if (client == null) return null;
        else return new ClientDTO(client);
    }


    @Override
    public Optional<String> create(ClientDTO dto) {
        if (clientDAO.findByPassportId(dto.getPassportId()) != null)
            return Optional.of("passportId is reserved");

        if (clientDAO.findByEmail(dto.getEmail()) != null)
            return Optional.of("email is reserved");

        Client client=new Client();
        updateFields(client,dto);
        clientDAO.save(client);
        dto.setId(client.getId());
        return Optional.empty();
    }

    @Override
    public Optional<String> update(ClientDTO dto) {
        Client client = clientDAO.findByPassportId(dto.getPassportId());
        if (client != null && client.getId() != dto.getId())  //check if passportId is unique
            return Optional.of("passportId is reserved");

        client = clientDAO.findByEmail(dto.getEmail());
        if (client != null && client.getId() != dto.getId())  //check if email is unique
            return Optional.of("email is reserved");

        client=clientDAO.findOne(dto.getId());
        updateFields(client,dto);
        clientDAO.update(client);
        return Optional.empty();
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
