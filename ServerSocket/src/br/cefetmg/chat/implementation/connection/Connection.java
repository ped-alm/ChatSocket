package br.cefetmg.chat.implementation.connection;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import br.cefetmg.chat.exception.ConnectionException;
import br.cefetmg.chat.interfaces.connection.IConnection;

/**
 * 
 * @author Vitor Rodarte & Pedro Almeida
 */

public class Connection implements IConnection{ 
    //Canal para troca de dados entre o cliente e o servidor
    private Socket pData;
    //Canal para atualização das mensagens do cliente
    //São dois canais para evitar conflitos
    private Socket pUpdate;
    //Socket unico do servidor
    private static ServerSocket sData;
    private static ServerSocket sUpdate;
    //Canal de saida de dados
    private ObjectOutputStream outData;
    //Canal de saida de updates
    private ObjectOutputStream update;
    //Canal de entrada de dados
    private ObjectInputStream inData; 
    
    public Connection() throws ConnectionException {
        try {
            pData = sData.accept();
            outData = new ObjectOutputStream(pData.getOutputStream());
            inData = new ObjectInputStream(pData.getInputStream());
            pUpdate = sUpdate.accept();
            update = new ObjectOutputStream(pUpdate.getOutputStream());
        } catch (IOException ex) {
            throw new ConnectionException("\nErro ao criar conexão com o Cliente: " + ex);
        }
    }

    @Override
    public void disconnect() throws ConnectionException {
        try {
            pData.close();
            pUpdate.close();
        } catch (IOException ex) {
            throw new ConnectionException("\nErro ao desconectar do Cliente: " + ex);
        }          
    }

    @Override
    public synchronized void sendData(String json) throws ConnectionException {
        try {    
            outData.writeObject(json);
            outData.flush();
        }
        catch (IOException ex) {
            throw new ConnectionException("\nErro ao enviar para o Cliente: " + ex);
        }
    }

    @Override
    public synchronized String receiveData() throws ConnectionException {
        try {
            return (String) inData.readObject();
        }
        catch (IOException | ClassNotFoundException ex) {
            throw new ConnectionException("\nErro ao receber do Cliente: " + ex);
        } 
    }
    
    @Override
    public synchronized void update(String json) throws ConnectionException {
        try {            
            update.writeObject(json);
            update.flush();
        }
        catch (IOException ex) {
            throw new ConnectionException("\nErro ao enviar para o Cliente: " + ex);
        }
    }
    
    public static void setServer(int port) throws ConnectionException {
        try {
            sData = new ServerSocket(port);
            sUpdate = new ServerSocket(port+1);
        } catch (IOException ex) {
            throw new ConnectionException("\nErro ao definir Socket do servidor: " + ex);
        }
    }

    public Socket getpData() {
        return pData;
    }

    public ObjectOutputStream getUpdate() {
        return update;
    }      
}
