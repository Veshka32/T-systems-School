package services.implementations;

import entities.Client;
import entities.Contract;
import entities.dto.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.implementations.ClientDAO;
import repositories.implementations.ContractDAO;
import services.ServiceException;
import services.interfaces.ClientServiceI;

import java.util.List;

@Service
@Transactional
public class ClientService implements ClientServiceI {
    private ClientDAO  clientDAO;
    private ContractDAO contractDAO;

    @Autowired
    public void setDAO(ClientDAO clientDAO, ContractDAO contractDAO) {
        this.clientDAO = clientDAO;
        this.contractDAO=contractDAO;
        this.contractDAO.setClass(Contract.class);
        this.clientDAO.setClass(Client.class);
    }

    @Override
    public Client get(int id) {
        return clientDAO.findOne(id);
    }

    @Override
    public Client findByPassport(String passport){
        return clientDAO.findByPassportId(passport);
    }

    @Override
    public void create(ClientDTO dto) throws ServiceException {
        if (clientDAO.isPassportExist(dto.getPassportId()))
            throw new ServiceException("such a passport Id already exists");

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
        return new ClientDTO(clientDAO.findOne(id));
    }

    @Override
    public List<Client> getAll() {
        return clientDAO.findAll();
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
