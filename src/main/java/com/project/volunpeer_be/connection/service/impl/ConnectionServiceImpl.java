package com.project.volunpeer_be.connection.service.impl;

import com.project.volunpeer_be.common.enums.StatusCode;
import com.project.volunpeer_be.common.util.PeerUtil;
import com.project.volunpeer_be.connection.dto.Connection;
import com.project.volunpeer_be.connection.dto.request.*;
import com.project.volunpeer_be.connection.dto.response.*;
import com.project.volunpeer_be.connection.service.ConnectionService;
import com.project.volunpeer_be.db.entity.PeerEntity;
import com.project.volunpeer_be.db.repository.PeerRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    PeerRepository peerRepository;

    @Autowired
    PeerUtil peerUtil;

    @Override
    public ConnectionListResponse getConnectionList(HttpServletRequest httpRequest) {
        PeerEntity peer = peerUtil.getPeerFromHttpRequest(httpRequest);
        HashSet<String> connectionIds = peer.getConnections();
        List<Connection> connectionList = new ArrayList<>();
        for (String connectionId : connectionIds) {
            PeerEntity connectionEntity = peerUtil.getPeerFromPeerId(connectionId);
            Connection connection = new Connection(connectionId, connectionEntity.getName());
            connectionList.add(connection);
        }

        ConnectionListResponse response = new ConnectionListResponse();
        response.setConnections(connectionList);
        response.setStatusCode(StatusCode.SUCCESS);
        return response;
    }

    @Override
    public SentConnectionListResponse getSentConnectionList(HttpServletRequest httpRequest) {
        PeerEntity peer = peerUtil.getPeerFromHttpRequest(httpRequest);
        HashSet<String> sentConnectionIds = peer.getSentConnectionRequests();
        List<Connection> sentConnectionList = new ArrayList<>();
        for (String sentConnectionId : sentConnectionIds) {
            PeerEntity sentConnectionEntity = peerUtil.getPeerFromPeerId(sentConnectionId);
            Connection sentConnection = new Connection(sentConnectionId, sentConnectionEntity.getName());
            sentConnectionList.add(sentConnection);
        }

        SentConnectionListResponse response = new SentConnectionListResponse();
        response.setSentConnections(sentConnectionList);
        response.setStatusCode(StatusCode.SUCCESS);
        return response;
    }

    @Override
    public ReceivedConnectionListResponse getReceivedConnectionList(HttpServletRequest httpRequest) {
        PeerEntity peer = peerUtil.getPeerFromHttpRequest(httpRequest);
        HashSet<String> receivedConnectionIds = peer.getReceivedConnectionRequests();
        List<Connection> receivedConnectionList = new ArrayList<>();
        for (String receivedConnectionId : receivedConnectionIds) {
            PeerEntity receivedConnectionEntity = peerUtil.getPeerFromPeerId(receivedConnectionId);
            Connection receivedConnection = new Connection(receivedConnectionId, receivedConnectionEntity.getName());
            receivedConnectionList.add(receivedConnection);
        }

        ReceivedConnectionListResponse response = new ReceivedConnectionListResponse();
        response.setReceivedConnections(receivedConnectionList);
        response.setStatusCode(StatusCode.SUCCESS);
        return response;
    }

    @Override
    public SendConnectionResponse sendConnection(SendConnectionRequest request, HttpServletRequest httpRequest) {
        PeerEntity sender = peerUtil.getPeerFromHttpRequest(httpRequest);
        HashSet<String> sentConnectionRequests = sender.getSentConnectionRequests();
        sentConnectionRequests.add(request.getPeerId());
        sender.setSentConnectionRequests(sentConnectionRequests);
        peerRepository.save(sender);

        PeerEntity receiver = peerUtil.getPeerFromPeerId(request.getPeerId());
        HashSet<String> receivedConnectionRequests = receiver.getReceivedConnectionRequests();
        receivedConnectionRequests.add(sender.getPeerId());
        receiver.setReceivedConnectionRequests(receivedConnectionRequests);
        peerRepository.save(receiver);

        SendConnectionResponse response = new SendConnectionResponse();
        response.setStatusCode(StatusCode.SUCCESS);
        return response;
    }

    @Override
    public CancelConnectionResponse cancelConnection(CancelConnectionRequest request, HttpServletRequest httpRequest) {
        PeerEntity sender = peerUtil.getPeerFromHttpRequest(httpRequest);
        HashSet<String> sentConnectionRequests = sender.getSentConnectionRequests();
        sentConnectionRequests.remove(request.getPeerId());
        sender.setSentConnectionRequests(sentConnectionRequests);
        peerRepository.save(sender);

        PeerEntity receiver = peerUtil.getPeerFromPeerId(request.getPeerId());
        HashSet<String> receivedConnectionRequests = receiver.getReceivedConnectionRequests();
        receivedConnectionRequests.remove(sender.getPeerId());
        receiver.setReceivedConnectionRequests(receivedConnectionRequests);
        peerRepository.save(receiver);

        CancelConnectionResponse response = new CancelConnectionResponse();
        response.setStatusCode(StatusCode.SUCCESS);
        return response;
    }

    @Override
    public AcceptConnectionResponse acceptConnection(AcceptConnectionRequest request, HttpServletRequest httpRequest) {
        PeerEntity receiver = peerUtil.getPeerFromHttpRequest(httpRequest);
        HashSet<String> receivedConnectionRequests = receiver.getReceivedConnectionRequests();
        receivedConnectionRequests.remove(request.getPeerId());
        receiver.setReceivedConnectionRequests(receivedConnectionRequests);
        HashSet<String> receiverConnections = receiver.getConnections();
        receiverConnections.add(request.getPeerId());
        receiver.setConnections(receiverConnections);
        peerRepository.save(receiver);

        PeerEntity sender = peerUtil.getPeerFromPeerId(request.getPeerId());
        HashSet<String> sentConnectionRequests = sender.getSentConnectionRequests();
        sentConnectionRequests.remove(receiver.getPeerId());
        sender.setSentConnectionRequests(sentConnectionRequests);
        HashSet<String> senderConnections = sender.getConnections();
        senderConnections.add(receiver.getPeerId());
        sender.setConnections(senderConnections);
        peerRepository.save(sender);

        AcceptConnectionResponse response = new AcceptConnectionResponse();
        response.setStatusCode(StatusCode.SUCCESS);
        return response;
    }

    @Override
    public RejectConnectionResponse rejectConnection(RejectConnectionRequest request, HttpServletRequest httpRequest) {
        PeerEntity receiver = peerUtil.getPeerFromHttpRequest(httpRequest);
        HashSet<String> receivedConnectionRequests = receiver.getReceivedConnectionRequests();
        receivedConnectionRequests.remove(request.getPeerId());
        receiver.setReceivedConnectionRequests(receivedConnectionRequests);
        peerRepository.save(receiver);

        PeerEntity sender = peerUtil.getPeerFromPeerId(request.getPeerId());
        HashSet<String> sentConnectionRequests = sender.getSentConnectionRequests();
        sentConnectionRequests.remove(receiver.getPeerId());
        sender.setSentConnectionRequests(sentConnectionRequests);
        peerRepository.save(sender);

        RejectConnectionResponse response = new RejectConnectionResponse();
        response.setStatusCode(StatusCode.SUCCESS);
        return response;
    }

    @Override
    public DeleteConnectionResponse deleteConnection(DeleteConnectionRequest request, HttpServletRequest httpRequest) {
        PeerEntity deleter = peerUtil.getPeerFromHttpRequest(httpRequest);
        HashSet<String> deleterConnections = deleter.getConnections();
        deleterConnections.remove(request.getPeerId());
        deleter.setConnections(deleterConnections);
        peerRepository.save(deleter);

        PeerEntity otherParty = peerUtil.getPeerFromPeerId(request.getPeerId());
        HashSet<String> otherPartyConnections = otherParty.getConnections();
        otherPartyConnections.remove(deleter.getPeerId());
        otherParty.setConnections(otherPartyConnections);
        peerRepository.save(otherParty);

        DeleteConnectionResponse response = new DeleteConnectionResponse();
        response.setStatusCode(StatusCode.SUCCESS);
        return response;
    }
}
