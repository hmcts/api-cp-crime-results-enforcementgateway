package uk.gov.hmcts.cp.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.cp.openapi.api.EnforcementHearingApi;
import uk.gov.hmcts.cp.openapi.model.ConfirmedHearing;
import uk.gov.hmcts.cp.openapi.model.ErrorResponse;
import uk.gov.hmcts.cp.openapi.model.HearingResultedRequest;
import uk.gov.hmcts.cp.openapi.model.HearingResultedResponse;
import uk.gov.hmcts.cp.openapi.model.NowsDataItemName;
import uk.gov.hmcts.cp.openapi.model.NowsDataItems;
import jakarta.validation.constraints.Size;
import uk.gov.hmcts.cp.openapi.model.EnforcementDetails;
import uk.gov.hmcts.cp.openapi.model.HearingResult;
import uk.gov.hmcts.cp.openapi.model.ParentGuardian;
import uk.gov.hmcts.cp.openapi.model.Payment;
import uk.gov.hmcts.cp.openapi.model.PaymentTerms;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.time.Instant;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class OpenApiObjectsTest {
    @Test
    void generated_error_response_should_have_expected_fields() {
        assertThat(ErrorResponse.class).hasDeclaredMethods("error", "message", "details", "traceId");
    }

    @Test
    void generated_error_response_timestamp_should_be_instant() throws Exception {
        Field timestampField = ErrorResponse.class.getDeclaredField("timestamp");

        assertThat(timestampField.getType())
                .as("timestamp field type")
                .isEqualTo(Instant.class);
    }

    @Test
    void generated_confirmed_hearing_should_have_expected_fields() {
        assertThat(ConfirmedHearing.class)
                .hasDeclaredFields("caseUrn", "courtHearingLocation", "dateOfHearing", "timeOfHearing");
    }

    @Test
    void generated_confirmed_hearing_date_of_hearing_should_be_local_date() throws Exception {
        Field dateOfHearingField = ConfirmedHearing.class.getDeclaredField("dateOfHearing");

        assertThat(dateOfHearingField.getType())
                .as("dateOfHearing field type")
                .isEqualTo(LocalDate.class);
    }

    @Test
    void generated_enforcement_hearing_api_should_have_expected_methods() {
        assertThat(EnforcementHearingApi.class).hasDeclaredMethods("postConfirmedHearing", "postHearingResulted");
        assertThat(EnforcementHearingApi.PATH_POST_CONFIRMED_HEARING).isEqualTo("/confirmedHearing");
        assertThat(EnforcementHearingApi.PATH_POST_HEARING_RESULTED).isEqualTo("/hearingResulted");
    }

    @Test
    void generated_hearing_resulted_request_should_have_expected_fields() {
        assertThat(HearingResultedRequest.class)
                .hasDeclaredFields("caseUrn", "dateOfHearing", "courtHearingLocation", "defendantDetails",
                        "employerDetails", "parentGuardianDetails", "paymentTerms", "enforcement", "results",
                        "nowsDataRequest");
    }

    @Test
    void generated_hearing_resulted_response_should_have_expected_fields() {
        assertThat(HearingResultedResponse.class)
                .hasDeclaredFields("caseUrn", "timestamp", "correlationId", "nowsDataItems");
    }

    @Test
    void generated_nows_data_item_name_should_have_twelve_values() {
        assertThat(NowsDataItemName.values()).hasSize(12);
        assertThat(NowsDataItemName.fromValue("Account Balance")).isEqualTo(NowsDataItemName.ACCOUNT_BALANCE);
    }

    @Test
    void generated_nows_data_items_account_balance_should_be_numeric() throws Exception {
        Field accountBalanceField = NowsDataItems.class.getDeclaredField("accountBalance");

        assertThat(Number.class).isAssignableFrom(accountBalanceField.getType());
    }

    @Test
    void enum_typed_properties_should_not_carry_size_constraints() {
        // @Size cannot validate an enum (HV000030 -> 500 on every request); see the LOCAL AMENDMENT in openapi-spec.yml
        final List<Method> sizedEnumGetters = Arrays.stream(new Class<?>[] {
                    PaymentTerms.class, EnforcementDetails.class, HearingResult.class, ParentGuardian.class, Payment.class})
                .flatMap(type -> Arrays.stream(type.getMethods()))
                .filter(m -> m.getReturnType().isEnum() && m.isAnnotationPresent(Size.class))
                .toList();

        assertThat(sizedEnumGetters).isEmpty();
    }
}
