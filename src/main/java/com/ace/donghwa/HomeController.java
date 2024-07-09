package com.ace.donghwa;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.sdk.client.NodeCache;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws UaException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	
	//태그호출
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws UaException, InterruptedException, ExecutionException {

		OpcUaClient client = OpcUaClient.create("opc.tcp://192.168.1.62:5660");

        // 클라이언트 연결
        client.connect().get();
        
        // 읽고자 하는 노드의 ID
        NodeId nodeId = new NodeId(2, "OPC_TEST.PLC.D.D902");

        // 노드 읽기
        CompletableFuture<DataValue> future = client.readValue(0, TimestampsToReturn.Both, nodeId);
        DataValue value = future.get();        
		
        System.out.println("value : "+value.getValue());
        
		return "home";
	}

	//채널호출
	@RequestMapping(value = "/opc", method = RequestMethod.GET)
	public String opc(Locale locale, Model model) throws UaException, InterruptedException, ExecutionException {

		OpcUaClient client = OpcUaClient.create("opc.tcp://192.168.1.62:5660");

        // 클라이언트 연결
        client.connect().get();
        
        // 읽고자 하는 변수 노드의 NodeId (예: "ns=2;s=ChannelName")
        NodeId nodeId = new NodeId(2, "OPC_TEST.ETC");

        // 노드 읽기
        VariableNode node = client.getAddressSpace().getVariableNode(nodeId);
        CompletableFuture<DataValue> futureValue = client.readValue(0, TimestampsToReturn.Both, nodeId);

        // 값 출력
        futureValue.thenAccept(value -> {
            System.out.println("Value: " + value.getValue().getValue());
        }).get();

        // 클라이언트 연결 종료
        client.disconnect().get();
        
        return "home";
	}
}

/*

// 읽고자 하는 노드의 ID
NodeId nodeId = new NodeId(2, "OPC_TEST.TEST_GROUP.VALUE1");

// 노드 읽기
CompletableFuture<DataValue> future = client.readValue(0, TimestampsToReturn.Both, nodeId);
DataValue value = future.get();
System.out.println("Read value: " + value.getValue());

// 노드 쓰기

VariableNode node = client.getAddressSpace().getVariableNode(nodeId);
StatusCode statusCode = node.writeValue(new DataValue(new Variant(42.0))).get();
System.out.println("Write status: " + statusCode);

// 클라이언트 연결 종료
client.disconnect().get(); 

*/