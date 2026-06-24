# Enforcement Hearing Gateway (API spec)

`api-cp-crime-caseingestion-enforcementgateway`

OpenAPI specification for the **CP ↔ Libra/GoB enforcement hearing gateway**. It documents the
contract used by the runtime service
[`service-cp-crime-caseingestion-enforcementgateway`](https://github.com/hmcts/service-cp-crime-caseingestion-enforcementgateway):

- **Hearing confirmation:** the `confirmedHearing` payload CP sends to Libra (GoB) when an enforcement
  case is allocated to a court hearing.
- **Hearing updates:** the same payload re-sent when an allocation is amended.

The spec is published as a generated artefact (OpenAPI generator) and consumed by the service as a
dependency, following the established `api-cp-crime-*` / `service-cp-crime-*` pairing.

> Owned by the **cp-case-ingestion-and-material** team. Created from the HMCTS template
> [`api-hmcts-crime-template`](https://github.com/hmcts/api-hmcts-crime-template).

> ⚠️ **Draft.** `src/main/resources/openapi/openapi-spec.yml` currently carries a draft
> `confirmedHearing` contract. The authoritative Libra-side endpoint contract is owned by Libra and
> must be reconciled.

## Naming

Follows the HMCTS api-template convention `api-{source}-[case-type]-{business-domain}-{entity}`:
`cp` · `crime` · **`caseingestion`** · `enforcementgateway`. The `caseingestion` segment reflects
the **owning team** (cp-case-ingestion-and-material, who hold all CP↔Libra enforcement integration,
inbound and outbound) rather than the literal direction of the call.

## Build

```bash
gradle build      # validates + generates from src/main/resources/openapi/openapi-spec.yml
```

OpenAPI linting runs in CI (`.github/workflows/lint-openapi.yml`); spec evolution rules are in
[`docs/OPENAPI-SPEC-VERSIONING.md`](./docs/OPENAPI-SPEC-VERSIONING.md) and
[`docs/API-VERSIONING-STRATEGY.md`](./docs/API-VERSIONING-STRATEGY.md).

## CI/CD

- `ci-draft.yml` — validate/lint on PRs.
- `ci-released.yml` — on a **published GitHub Release**, publish the versioned spec artefact.
- `code-analysis.yml`, `codeql.yml`, `lint-openapi.yml`, `secrets-scanner.yml`, `auto-merge-dependabot.yml`.

`main` and `team/*` branches are protected and require at least one approving review.

## License

MIT — see [LICENSE](LICENSE).
