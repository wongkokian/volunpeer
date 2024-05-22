package com.project.volunpeer.peer.service;

import com.project.volunpeer.peer.dto.request.PeerCreateRequest;
import com.project.volunpeer.peer.dto.request.PeerDetailsRequest;
import com.project.volunpeer.peer.dto.response.PeerCreateResponse;
import com.project.volunpeer.peer.dto.response.PeerDetailsResponse;

public interface PeerService {
    PeerCreateResponse createPeer(PeerCreateRequest request);

    PeerDetailsResponse getPeerDetails(PeerDetailsRequest request);
}
