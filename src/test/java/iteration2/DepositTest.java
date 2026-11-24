package iteration2;

import generators.RandomData;
import models.*;
import org.junit.jupiter.api.Test;
import requests.AdminCreateUserRequester;
import requests.CreateAccountRequester;
import requests.DepositRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class DepositTest extends BaseTest {

    @Test
    public void depositTest() {

        CreateUserRequest userRequest = CreateUserRequest.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new AdminCreateUserRequester(
                RequestSpecs.adminSpec(),
                ResponseSpecs.entityWasCreated())
                .post(userRequest);

        AccountResponse accountResponse = new CreateAccountRequester(
                RequestSpecs.authAsUser(userRequest.getUsername(), userRequest.getPassword()),
                ResponseSpecs.entityWasCreated())
                .post()
                .extract().as(AccountResponse.class);

        double amount = RandomData.getValidAmount();

        DepositRequest depositRequest = DepositRequest.builder()
                .id(accountResponse.getId())
                .balance(amount)
                .build();

        DepositResponse depositResponse = new DepositRequester(
                RequestSpecs.authAsUser(userRequest.getUsername(), userRequest.getPassword()),
                ResponseSpecs.requestReturnsOK())
                .post(depositRequest)
                .extract().as(DepositResponse.class);

        softly.assertThat(depositResponse.getBalance()).isEqualTo(amount);
    }
}