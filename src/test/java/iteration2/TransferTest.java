package iteration2;

import generators.RandomData;
import models.*;
import org.junit.jupiter.api.Test;
import requests.*;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class TransferTest extends BaseTest {

    @Test
    public void userTransferTest() {
        // 1. Создаем юзера
        CreateUserRequest userRequest = CreateUserRequest.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();
        new AdminCreateUserRequester(RequestSpecs.adminSpec(), ResponseSpecs.entityWasCreated()).post(userRequest);

        // 2. Создаем два счета
        AccountResponse sender = new CreateAccountRequester(
                RequestSpecs.authAsUser(userRequest.getUsername(), userRequest.getPassword()),
                ResponseSpecs.entityWasCreated()).post(null).extract().as(AccountResponse.class);

        AccountResponse receiver = new CreateAccountRequester(
                RequestSpecs.authAsUser(userRequest.getUsername(), userRequest.getPassword()),
                ResponseSpecs.entityWasCreated()).post(null).extract().as(AccountResponse.class);

        // 3. Перевод
        TransferRequest transferRequest = TransferRequest.builder()
                .senderAccountId(sender.getId())
                .receiverAccountId(receiver.getId())
                .amount(50.00)
                .build();

        TransferResponse transferResponse = new TransferRequester(
                RequestSpecs.authAsUser(userRequest.getUsername(), userRequest.getPassword()),
                ResponseSpecs.requestReturnsOK())
                .post(transferRequest)
                .extract().as(TransferResponse.class);

        // 4. Проверка
        softly.assertThat(transferResponse.getAmount()).isEqualTo(50.00);
    }
}