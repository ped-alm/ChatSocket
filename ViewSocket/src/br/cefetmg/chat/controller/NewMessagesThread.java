/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.chat.controller;

import br.cefetmg.chat.domain.Message;
import br.cefetmg.chat.domain.Room;
import br.cefetmg.chat.exception.BusinessException;
import br.cefetmg.chat.exception.ConnectionException;
import br.cefetmg.chat.implementation.service.MessageBusiness;
import br.cefetmg.chat.implementation.service.RoomBusiness;
import br.cefetmg.chat.interfaces.connection.IConnection;
import br.cefetmg.chat.interfaces.service.IMessageBusiness;
import br.cefetmg.chat.interfaces.service.IRoomBusiness;
import br.cefetmg.chat.view.MainView;
import br.cefetmg.chat.view.MainView;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 *
 * @author aluno
 */
public class NewMessagesThread implements Runnable{
    
    private IConnection c;
    public MainView p;
    
    public NewMessagesThread(IConnection c, MainView p){
        this.c = c;
        this.p = p;
    }
    
    @Override
    public void run() {
        try {
            String update = (String)c.receiveMensagens();
            switch(update){
                case "msg":
                    p.loadRoom(p.getCurrentRoom());
                    break;
                case "sala":
                    p.showHome();
                    break;
                case "usuarios":
                    Room r = p.getCurrentRoom();
                    p.showHome();
                    p.loadRoom(r);
                    break;
            }
        } catch (ConnectionException | BusinessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
}
