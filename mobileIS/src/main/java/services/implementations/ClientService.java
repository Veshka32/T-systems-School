/**
 * This class implements {@code ClientServiceI} interface.
 * It is a service-layer class for manipulating with {@code Client} entities.
 * <p>
 *
 * @author Natalia Makarchuk
 */
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

    /**Returns json representation of {@code Client} with specific passport (passport supposed to be unique)
     *
     * @param passport unique passport value  of desired client
     * @return String in json format
     */
    @Override
    public String getJsonByPassport(String passport) {
        Gson gson = new Gson();
        JsonElement element = new JsonObject();
        try {
            ClientDTO dto = getByPassport(passport);
            if (dto == null) {
                setError(element, "there is no such client");
            } else {
                setSuccess(element, gson.toJsonTree(dto));
            }
        } catch (NumberFormatException e) {
            setError(element, "must be 10 digits");
        }
        return gson.toJson(element);
    }

    /**Returns data transfer object contains public properties of {@code Client} with specific passport (passport supposed to be unique)
     *
     * @param passport unique passport value  of desired client
     * @return data transfer object
     */
    @Override
    public ClientDTO getByPassport(String passport) {
        if (!passport.matches("^[0-9]{10}")) throw new NumberFormatException();
        Client client = clientDAO.findByPassportId(passport);
        if (client == null) return null;
        else return new ClientDTO(client);
    }

    /**Returns json representation of {@code Client} who has {@code Contract} defined by specific phone number (phone supposed to be unique)
     *
     * @param phone unique phone number
     * @return String in json format
     */
    @Override
    public String getJsonByPhone(String phone) {
        Gson gson = new Gson();
        JsonElement element = new JsonObject();
        try {
            ClientDTO dto = getByPhone(phone);
            if (dto == null) {
                setError(element, "there is no such client");
            } else {
                setSuccess(element, gson.toJsonTree(dto));
            }
        } catch (NumberFormatException e) {
            setError(element, "must be 10 digits");
        }
        return gson.toJson(element);
    }

    /**Returns data transfer object contains public properties of {@code Client}
     * who has {@code Contract} with specific phone number (phone number supposed to be unique)
     *
     * @param phone unique phone number value of desired client
     * @return data transfer object
     */
    @Override
    public ClientDTO getByPhone(String phone) {
        if (!phone.matches("^[0-9]{10}")) throw new NumberFormatException();
        Client client = contractDaoI.findClientByPhone(Long.parseLong(phone));
        if (client == null) return null;
        else return new ClientDTO(client);
    }

    /**
     * Create new {@code Client} based on dto properties
     *
     * @param dto data transfer object contains required properties
     * @return empty Optional if client is successfully created or error message if not
     */

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

    /**
     * Update all fields in corresponding {@code Client}  with values from data transfer object
     *
     * @param dto data transfer object contains client id and properties
     * @return empty Optional if client is successfully updated or error message if not
     */

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

    /**
     * Delete {@code Client} with specific id from database and all its corresponding {@code Contract} and {@code User}
     *
     * @param id id of client to delete
     */
    @Override
    public void delete(int id){
        clientDAO.deleteById(id);
    }

    /**
     * Construct data transfer object for {@code Client} with specific id, including list with client's contracts
     *
     * @param id client id in database
     * @return data transfer object contains client properties
     */
    @Override
    public ClientDTO getDTO(int id){
        ClientDTO dto = new ClientDTO(clientDAO.findOne(id));
        dto.setContractsList(contractDaoI.getClientContracts(id));
        return dto;
    }

    /**
     * Construct object contains clients in specific range from database and additional info (total number of clients in database)
     *
     * @param currentPage current page on view to build
     * @param rowPerPage  number of items for one page (equals total number of items in {@code PaginateHelper}
     * @return {@code PaginateHelper} with clients in specific range and additional info
     */

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

    /*
     * set error status and error message into json String
     */
    private void setError(JsonElement element, String message) {
        element.getAsJsonObject().addProperty("status", "error");
        element.getAsJsonObject().addProperty("message", message);
    }

    /*
     * set success status and desired value into json String
     */
    private void setSuccess(JsonElement element, JsonElement tree) {
        element.getAsJsonObject().addProperty("status", "success");
        element.getAsJsonObject().add("client", tree);
    }
}
