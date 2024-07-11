package com.ace.donghwa;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Locale locale, Model model) {
        return "home";
    }

    @RequestMapping(value = "/opc", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> opc() throws UaException, InterruptedException, ExecutionException {

        OpcUaClient client = OpcUaClient.create("opc.tcp://192.168.1.62:5660");

        // 클라이언트 연결
        client.connect().get();

        // 단일 노드 읽기
        NodeId singleNodeId = new NodeId(2, "OPC_TEST.TEST_GROUP.VALUE1");
        CompletableFuture<DataValue> singleFuture = client.readValue(0, TimestampsToReturn.Both, singleNodeId);
        DataValue singleValue = singleFuture.get();
        System.out.println("Read value: " + singleValue.getValue());
        
        // 여러 노드의 ID
        List<NodeId> nodeIds = Arrays.asList(
            new NodeId(2, "OPC_TEST.PLC.D.D902"),
            new NodeId(2, "OPC_TEST.PLC.D.D903"),
            new NodeId(2, "OPC_TEST.PLC.D.D904")
        );

        // 여러 노드 읽기
        List<CompletableFuture<DataValue>> futures = nodeIds.stream()
            .map(nodeId -> client.readValue(0, TimestampsToReturn.Both, nodeId))
            .collect(Collectors.toList());

        // CompletableFuture를 사용하여 모든 값을 한꺼번에 대기
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.get();

        // 각 노드의 값을 출력 및 저장
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("singleValue", singleValue.getValue().getValue());
        Map<String, Object> multiValues = new HashMap<>();
        for (int i = 0; i < nodeIds.size(); i++) {
            DataValue value = futures.get(i).get();
            multiValues.put(nodeIds.get(i).toString(), value.getValue().getValue());
        }
        responseMap.put("multiValues", multiValues);
        System.out.println("읽어온 노드값 : " + multiValues);
        // 클라이언트 연결 종료
        client.disconnect().get();

        return responseMap;
    }

    @RequestMapping(value = "/write", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> write(@RequestParam("nodeId") String nodeIdStr, @RequestParam("value") int valueStr) 
    		throws UaException, InterruptedException, ExecutionException {

    	System.out.println("nodeIdStr : "+nodeIdStr+"// valueStr : "+valueStr);
    	
        OpcUaClient client = OpcUaClient.create("opc.tcp://192.168.1.62:5660");
        
        System.out.println(" client.getAddressSpace() : "+client.getAddressSpace());
        System.out.println(" client.getConfig() : "+client.getConfig());
        System.out.println(" client.getNamespaceTable() : "+client.getNamespaceTable());
        System.out.println(" client.getSubscriptionManager() : "+client.getSubscriptionManager());
        
        // 클라이언트 연결
        client.connect().get();

        // 노드 ID 생성
        NodeId nodeId = new NodeId(2, nodeIdStr);

        // 값을 설정
        CompletableFuture<DataValue> future = client.readValue(0, TimestampsToReturn.Both, nodeId);

        System.out.println("future Value : "+future.get().getValue());
        
        DataValue dataValue = new DataValue(new Variant(valueStr));
        
        System.out.println("쓰는 데이터 : "+dataValue.getValue());
        
        UaVariableNode variableNode = client.getAddressSpace().getVariableNode(nodeId);

        // 값을 쓰기
//        variableNode.writeValue(dataValue);
        variableNode.setValue(dataValue);
        
        // 클라이언트 연결 종료
        client.disconnect().get();
        
        // 응답 생성
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Written value: " + valueStr + " to node: " + nodeIdStr);

        return response;
    }
}

