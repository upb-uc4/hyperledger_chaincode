# [v1.0.0](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.19.1...v1.0.0) (2021-03-15)

Release (no changes to previous version)

# [v0.19.1](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.19.0...v0.19.1) (2021-03-02)

## Feature
- check transactions for timestamp validity


## Refactor
- consolidate transactions and outsource transaction validation

# [v0.19.0](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.18.2...v0.19.0) (2021-03-01)

## Refactor
- [errors related to operation lifecycle](https://github.com/upb-uc4/api/pull/115)
- update readme (state java version earlier)

# [v0.18.2](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.18.1...v0.18.2) (2021-02-26)

## Bugfix
- wrong info in enum_error for Admissions (should be Course/Exam, was OralExam/WrittenExam)

## Refactor
- improve testCoverage --> 80%
- code clean up
- outsource methods to static helpers

# [v0.18.1](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.17.0...v0.18.1) (2021-02-23)

## Feature
- add exam contract
- add exam result contract
- add exam admission contract
- add operation pending checks and errors

## Bugfix
- deny rejection if reject message empty

## Refactor
- update readme
- update timestamps (java instant)
- replace type tokens by utilizing array deserialization instead

# [v0.17.0](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.16.8...v0.17.0) (2021-01-29)

## Feature
- Restrict GroupContract transaction access 
  - only users with hlf.attribut sysAdmin=true are allowed to manipulate groups

## Bugfix
- operationIds now no longer contain "=" signs

# [v0.16.8](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.16.7...v0.16.8) (2021-01-27)

## Feature
- adding System Group to required approvals for most transactions

# [v0.16.7](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.16.6...v0.16.7) (2021-01-26)

## Bugfix
- only users required to approve an operation are now allowed to approve the operation


# [v0.16.6](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.16.5...v0.16.6) (2021-01-26)

## Bugfix
- only users required to approve an operation are now allowed to reject the operation

# [v0.16.5](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.16.4...v0.16.5) (2021-01-25)

## Refactor
- refactor approveTransaction
- split approveTransaction into initiateOperation and approveOperation
- rename rejectTransaction to rejectOperation
- add operationIds, involvedEnrollmentId, and states filters to getOperations

# [v0.16.4](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.16.3...v0.16.4) (2021-01-25)

## Bugfix
- transactions now check if the operation is pending before executing

# [v0.16.3](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.16.2...v0.16.3) (2021-01-20)

## Feature
- System Group has to approve addMatriculationData
- System Group, Admin Group and designated user have to approve addEntriesToMatriculationData

# [v0.16.2](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.16.1...v0.16.2) (2021-01-19)

## Feature
- executing client now implicitly approves the transaction
- implicit approval of users
- remove additional information from users rejectionMessage
- approveTransaction() set initiator default as client executing the transaction

## Bugfix
- operationsKey is generated in a different way (no spaces in parameters)
- gson List/Array parsing problem (List--> null)

# [v0.16.1](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.16.0...v0.16.1) (2021-01-19)

## Bugfix
- switch to base64url
- adjust hash delimiter to ":"

# [v0.16.0](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.15.2...v0.16.0) (2021-01-18)

Release (no changes to previous version)

# [v0.15.2](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.15.1...v0.15.2) (2021-01-15)

## Feature
- set operation state to FINISHED on successful transaction execution

## Bugfix
- admin group name: "admin" --> "Admin"

## Refactor
- read transaction_funcitonName from stub
- read annotated contract name from static contract name

## Usability
- 

# [v0.15.1](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.15.0...v0.15.1) (2021-01-12)

## Feature
- extend operation contract by timestamps, initiator, and disapproval
- extend getOperations filter by initiator, existing approvals, and missing approvals

## Bugfix
- add parameter validation error for invalid number of parameters

## Refactor
- rename ApprovalContract to OperationContract

## Usability
- 

# [v0.15.0](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.14.2...v0.15.0) (2021-01-05)

## Feature
- add support for group approvals
- reenable approval Checks for all transactions
- approveTransaction now checks parameter validity

## Bugfix
- fix extraction of enrollmentId from clientId

## Refactor
- introduce AccessManager
- folder structure

## Usability
- 

# [v0.14.2](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.14.1...v0.14.2) (2020-12-15)

## Feature
- addMatriculationData now checks approvals
- add groups to approvals
- add submission result


## Bugfix
- 

## Refactor
- 

## Usability
- 

# [v0.14.1](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.13.1...v0.14.1) (2020-12-15)

## Feature
- add group Contract

## Bugfix
- 

## Refactor
- 

## Usability
- Increase Test Code Coverage Threshold

# [v0.13.1](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.13.0...v0.13.1) (2020-12-04)

## Feature
- add admission Contract

## Bugfix
- add getAllExaminationRegulations functionality

## Refactor
- centralize Test Creation in base Class
- remove duplicate code (EmptyParameterError)

## Usability
- adjust github workflows to always upload TestCoverage Report, even when not meeting the 70% criterion

# [v0.13.0](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.12.3...v0.13.0) (2020-11-24)

## Bugfix
- fix general Matriculation Contract Error
- hotfix version error by replacing resource version with hardcoded string


# [v0.12.3](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.12.2...v0.12.3) (2020-11-23)

## Feature
- add fieldOfStudy validation to MatriculationData transactions
- add tests for fieldOfStudy validation
- add tests for unprocessable ledger state
- read version from git-tags

## Refactor
- extend test model to allow for specification of client id
- extend test setup model to allow for specification of contract for initial ledger entries

## Bugfix
- getExaminationRegulations("[[]]") now correctly returns the full list of existing Examination Regulations.


# [v0.12.2](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.11.5...v0.12.2) (2020-11-10)

- provide code via release assets when tagging
- add coverage test and present reports in test runs
- add installDist-test
- read version from referenced tag
- restructure gradle project to include our groupName to distinguish our project from hlf
- remove obsolete schadowJar from build

# [v0.12.0](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.11.5...v0.12.0) (2020-11-10)

Release (no changes to previous version)



# [v0.11.5](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.11.4...v0.11.5) (2020-11-04)

## Refactor
- rename ```Module``` model to ```ExaminationRegulationModule```
- rename matriculation-contract transaction parameters to be consistent with the api



# [v0.11.4](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.11.3...v0.11.4) (2020-11-04)

## Feature
- add examination-regulation contract
- add examination-regulation contract tests


# [v0.11.3](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.11.2...v0.11.3) (2020-11-03)

## Feature
- add approval contract
- add approval contract tests


# [v0.11.2](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.11.1...v0.11.2) (2020-10-30)

## Refactor
- replace field of study enum by string, allowing for configurable fields of study


# [v0.11.1](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.11.0...v0.11.1) (2020-10-29)

## Refactor
- replace makeshift composite keys by fabric's built in composite keys


# [v0.11.0](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.10.1...v0.11.0) (2020-10-26)

Release (no changes to previous version)


# [v0.10.1](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.10.0...v0.10.1) (2020-10-20)

## Feature
- add version chaincode



# [v0.10.0](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.9.2...v0.10.0) (2020-10-19)

## Bug Fixes
- fix error related to updated fabric shim version

## Refactor
- purge courses chaincode



# [v0.9.2](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.9.1...v0.9.2) (2020-10-01)

## Bug Fixes
- fix MatriculationDataContract and CertificateContract storing data under the same key (i.e. enrollmentId)

## Refactor
- refactor tests to do common setup only once per contract



# [v0.9.1](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.9.0...v0.9.1) (2020-09-29)

## Feature
- add certificate contract
- add certificate contract tests

## Refactor
- refactor matriculation-data contract tests
- update matriculation-data contract transaction documentation
- out-source errors to utilities



# [v0.9.0](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.8.0...v0.9.0) (2020-09-28)


## Refactor
- pseudonymize MatriculationData by replacing first name, last name, and birth date by an enrollment-ID
- move MatriculationData from private-data collection to contract-wide ledger
- move transaction arguments from transient-data field to arguments



# [v0.8.0](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.7.2...v0.8.0) (2020-09-14)

## Bug Fixes
- fix tests not throwing error when querying ```getPrivateDataUTF8``` for empty string [#25](https://github.com/upb-uc4/hlf-chaincode/pull/25)



# [v0.7.2](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.7.1...v0.7.2) (2020-09-9)

## Bug Fixes
- split up *collections_config* for dev/production network [#21](https://github.com/upb-uc4/hlf-chaincode/pull/21)

## Refactor
- refactor ```addEntryToMatriculation``` to take list of entries [#20](https://github.com/upb-uc4/hlf-chaincode/pull/20)
- refactor error format to conform to api [#37](https://github.com/upb-uc4/api/pull/37), [#39](https://github.com/upb-uc4/api/pull/39)



# [v0.7.1](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.7.0...v0.7.1) (2020-09-7)

## Feature
- move sensitive data to transient data field and store data in private data collection [#17](https://github.com/upb-uc4/hlf-chaincode/pull/17)

## Refactor
- refactor tests for private data transactions



# [v0.7.0](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.6.0.1...v0.7.0) (2020-08-31)

## Refactor
- add transaction comments
- establish consistency among invalid parameter error reasons
- establish consistency for code style (if statements)
- establish consistency between variable names and java naming convention
- remove unused logger
- remove unnecessary conditions



# [v0.6.0](https://github.com/upb-uc4/hyperledger_chaincode/compare/v0.5.0...v0.6.0) (2020-08-19)

## Feature
- add licence
- split up errors to be unambiguous
- add tests

## Bug Fixes
- prevent invalid paramters from appearing multiple times in the same error



# v0.5.0 (2020-08-11)

## Feature
- add UC4.MatriculationData chaincode
  - add addMatriculationData transaction
  - add updateMatriculationData transaction
  - add getMatriculationData transaction
  - add addEntryToMatriculationData transaction
- add GenericError
- add UC4.MatriculationData tests

## Refactor
- rework DetailedError to conform to API specification
- rework MatriculationData to conform to API specification
  - rework SubjectMatriculation
  - delete MatriculationInterval
- rework test format to extensively outsource JSON-IO
