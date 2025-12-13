package co.parameta.technical.test.soap.endpoint;

import co.parameta.technical.test.commons.pojo.EmployeeResponsePojo;
import co.parameta.technical.test.commons.pojo.SaveEmployeeRequestPojo;
import co.parameta.technical.test.soap.service.ISaveEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import static co.parameta.technical.test.soap.util.constant.Constant.NAMESPACE_URI;

@Endpoint
@RequiredArgsConstructor
public class EmployeeEndpoint {

    private final ISaveEmployeeService iSaveEmployeeService;


    @PayloadRoot(
            namespace = NAMESPACE_URI,
            localPart = "SaveEmployeeRequest"
    )
    @ResponsePayload
    public EmployeeResponsePojo guardarEmpleado(
            @RequestPayload SaveEmployeeRequestPojo request
    ) {
        return iSaveEmployeeService.saveEmployee(request.getEmployee());
    }


}
