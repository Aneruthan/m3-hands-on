# PROMPTS.md

## CS5013 Module 3 – AI for Code Generation and Autocompletion

This document records the AI-assisted work performed for the Module 3 hands-on exercise.

---

## Part A – Record and Autocompletion

### Initial Copilot suggestion

In `UserDTO.java`, after starting the record with:

```java
public record UserDTO(
```

Copilot initially suggested:

```java
public record UserDTO(
    String username,
    String email,
    String firstName,
    String lastName
) {
    public static UserDTO fromUser(User user) {
        return new UserDTO(
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName()
        );
    }
}
```

### Evaluation

This suggestion did not match the actual `User.java` implementation. The project contains the fields:

```text
long id
String name
String email
boolean active
```

The initial suggestion therefore contained fields and getter methods that did not exist in the project, such as `getUsername()`, `getFirstName()`, and `getLastName()`.

This demonstrated why ghost text should be read and compared with the existing code before accepting it.

### Suggestion after opening `User.java`

After opening `User.java` in a second tab and retriggering completion, Copilot produced:

```java
long id,
String name,
String email,
boolean active
) {
    public static UserDTO fromUser(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.isActive());
    }
}
```

This matched the actual fields and accessor methods in `User.java`, so it was accepted.

---

## Part B – `fromUser` Mapper

The accepted mapper was:

```java
public static UserDTO fromUser(User user) {
    return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.isActive());
}
```

A small `main` method was also added:

```java
public static void main(String[] args) {
    User user = new User(1, "Alice", "alice@example.com", true);
    UserDTO dto = fromUser(user);
    System.out.println(dto);
}
```

### Verification

Compilation command:

```text
javac -d build src/User.java src/UserDTO.java
```

Compilation succeeded.

Execution command:

```text
java -cp build UserDTO
```

Output:

```text
UserDTO[id=1, name=Alice, email=alice@example.com, active=true]
```

No hallucinated-field or missing-method compilation error occurred in the accepted version.

---

## Part C – `OrderController.java`

### `getOrderById`

Copilot suggested:

```java
return store.get(id);
```

This was consistent with the existing `Map<Long, Order> store` and the documented behavior of returning the matching order or `null`.

### `createOrder`

Copilot generated:

```java
if (item == null || item.trim().isEmpty()) {
    throw new IllegalArgumentException("Item must not be null or empty");
}
if (qty <= 0) {
    throw new IllegalArgumentException("Quantity must be a positive integer");
}
Order order = new Order(nextId++, item, qty);
store.put(order.id(), order);
return order;
```

This was consistent with the existing `Order` record, `store`, and `nextId`.

### Testing

The provided JUnit tests were run using:

```text
make test
```

All four tests passed:

```text
listOrdersReturnsSeedOnStartup()
getOrderByIdReturnsSeedForIdOne()
createOrderAddsAndReturnsOrder()
getOrderByIdReturnsNullForMissing()
```

Result: **4 tests successful, 0 failed.**

An initial attempt from Windows Command Prompt failed because the Makefile uses Unix-style commands such as `mkdir -p`. Running it from Git Bash succeeded.

---

## Part D – AI-Assisted Git

### Original AI-generated commit message

```text
Implement order retrieval and creation in OrderController; add UserDTO fields and mapping method
```

### Edited commit message

```text
Implement order retrieval and creation in OrderController; add UserDTO fields and mapping method

Completed the missing controller methods and added the UserDTO mapping required for the hands-on exercises. I made these changes to test AI-generated Java boilerplate and verify the implementation with the provided JUnit tests.
```

The changes were committed on the feature branch:

```text
feat/m3-hands-on
```

---

## Pull Request – AI-Generated Summary

Copilot Chat was asked to review the actual project changes and draft a pull request summary. It inspected the branch, relevant source files and tests, and ran the project's `make test` command.

### Original Copilot-generated PR summary

```markdown
## Summary

- Completed the missing order retrieval and creation behavior in OrderController.java.
- Added the DTO mapping in UserDTO.java to convert a `User` into a `UserDTO`.
- Documented the AI-assisted workflow and verification notes in PROMPTS.md.

## Main implementation details

- Implemented `getOrderById(long)` to return the matching order from the in-memory store, or `null` when no order exists.
- Implemented `createOrder(String, int)` with validation for:
  - non-empty item names
  - positive quantities
- Added the next-ID allocation and storage logic so new orders are persisted in the controller’s map and returned to the caller.
- Added `UserDTO.fromUser(User)` to map the user’s `id`, `name`, `email`, and `active` fields into the DTO.
- Included a simple `main` method in UserDTO.java to demonstrate the DTO conversion output.

## Testing / verification

- Verified the exercise with the project’s Java test workflow using `make test`.
- The project notes confirm the provided JUnit tests in OrderControllerTest.java pass:
  - `listOrdersReturnsSeedOnStartup`
  - `getOrderByIdReturnsSeedForIdOne`
  - `getOrderByIdReturnsNullForMissing`
  - `createOrderAddsAndReturnsOrder`

## Purpose

This change completes the Module 3 hands-on exercise by implementing the required Java logic and validating the generated code against the project’s seeded data and test suite. The goal is to demonstrate AI-assisted development in a realistic codebase while confirming the generated implementation matches the actual domain model and behavior.
```

### Edited PR summary used in the pull request

```markdown
## Summary

- Completed `getOrderById` and `createOrder` in `OrderController`.
- Added `UserDTO.fromUser(User)` to map `id`, `name`, `email`, and `active`.
- Added a small `main` method to demonstrate the DTO conversion.
- Documented the AI-assisted development process in `PROMPTS.md`.

## Testing

- Ran the provided test suite using `make test`.
- All four JUnit tests passed successfully.

## Purpose

These changes complete the Module 3 hands-on exercise. I made these changes to evaluate how accurately AI can generate Java boilerplate when given the correct project context, and I verified the generated code through compilation and the provided tests.
```

---

## Part E – Branch Name Suggestion

### Prompt

```text
Suggest a branch name for this issue:

"customer wants to be able to close their account permanently"

Format: prefix/short-kebab-slug

The prefix must be one of:
feat, fix, chore, docs, refactor
```

### Copilot response

```text
## Suggested branch name

feat/close-account-permanently

This matches the required format: prefix/short-kebab-slug.
```

### Evaluation

I agree with the suggestion because it follows the required format, uses an allowed `feat` prefix, and clearly describes the requested feature.

The branch name was evaluated only and was not created because Part E required a suggestion and evaluation.

---

## Reflection

AI was useful for generating repetitive Java boilerplate and completing straightforward methods. Once `User.java` was available as context, Copilot correctly inferred the fields needed for `UserDTO` and generated the corresponding mapper. It also correctly completed the order lookup and order creation logic in `OrderController`.

The initial `UserDTO` suggestion demonstrated an important limitation. It suggested plausible but nonexistent fields and methods such as `username`, `firstName`, `lastName`, `getUsername()`, `getFirstName()`, and `getLastName()`. This showed that AI-generated code can look reasonable while still being inconsistent with the actual project.

I therefore checked the generated code against the existing source files and verified it through compilation and tests. The provided JUnit tests confirmed that all four controller tests passed.

The AI-generated commit message was reviewed rather than accepted without modification. I kept the useful subject and added my own explanation of why the changes were made.

For the PR summary, Copilot Chat was used to inspect the actual project and generate a summary based on the branch contents and test results. I then edited the generated summary to make it more concise and to include my own explanation of the purpose of the changes.

Overall, the exercise reinforced that AI assistance should be treated as a drafting aid rather than automatically correct output: read the suggestion, compare it with the existing code, compile it, and test it before relying on it.
