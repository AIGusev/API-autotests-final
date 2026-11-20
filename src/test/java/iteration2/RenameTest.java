package iteration2;

import generators.RandomData;
import models.*;
import org.junit.jupiter.api.Test;
import requests.*;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class RenameTest extends BaseTest {

    @Test
    public void renameTest() {
        // 1. Создаем юзера
        CreateUserRequest userRequest = CreateUserRequest.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();
        new AdminCreateUserRequester(RequestSpecs.adminSpec(), ResponseSpecs.entityWasCreated()).post(userRequest);

        // 2. Меняем имя
        RenameRequest renameRequest = RenameRequest.builder().name("Artem Gusev").build();
        RenameResponse renameResponse = new RenameRequester(
                RequestSpecs.authAsUser(userRequest.getUsername(), userRequest.getPassword()),
                ResponseSpecs.requestReturnsOK())
                .post(renameRequest)
                .extract().as(RenameResponse.class);

        // 3. Проверка
        softly.assertThat(renameResponse.getName()).isEqualTo("Artem Gusev");
    }
}