# PROMPTS.md

## CS5013 Module 3 – AI for Code Generation and Autocompletion

This file records the AI interactions, generated code observations, commit-message assistance, PR-description work, and reflection for the Module 3 hands-on exercise.

---

## Part A – Record and Autocompletion

### 1. Initial ghost text in `UserDTO.java`

After leaving the empty record header as:

```java
public record UserDTO(
```

Copilot first suggested:

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

### Observation

This suggestion did not match the actual `User.java` class. The actual fields are `long id`, `String name`, `String email`, and `boolean active`. The suggestion therefore contained hallucinated fields and methods such as `getUsername()`, `getFirstName()`, and `getLastName()`.

I did not blindly trust this suggestion.

### 2. Ghost text after opening `User.java`

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

This matched the actual fields and accessor methods in `User.java`, so I accepted it.

---

## Part B – `fromUser` Mapper

The accepted mapper was:

```java
public static UserDTO fromUser(User user) {
    return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.isActive());
}
```

### Verification

Compilation:

```text
javac -d build src/User.java src/UserDTO.java
```

Compilation succeeded.

Running:

```text
java -cp build UserDTO
```

Output:

```text
UserDTO[id=1, name=Alice, email=alice@example.com, active=true]
```

There were no hallucinated-field or missing-method compilation errors in the accepted version.

A small `main` method was also added to construct a `User`, call `fromUser`, and print the resulting `UserDTO`.

---

## Part C – `OrderController.java`

### `getOrderById`

Copilot suggested:

```java
return store.get(id);
```

This was consistent with the existing `Map<Long, Order> store` and the method's documented behavior.

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

This was consistent with the existing `Order` record, `store`, and `nextId` fields.

### Testing

The provided JUnit tests were run using:

```text
make test
```

The tests passed:

```text
listOrdersReturnsSeedOnStartup()          OK
getOrderByIdReturnsSeedForIdOne()        OK
createOrderAddsAndReturnsOrder()         OK
getOrderByIdReturnsNullForMissing()      OK
```

Result: **4 tests successful, 0 failed.**

An initial attempt from Windows Command Prompt failed because the Makefile uses Unix-style commands such as `mkdir -p`. Running the same command from Git Bash worked successfully.

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

The commit was created as part of the feature-branch workflow.

---

## PR Description / Summary

GitHub Copilot's PR Summary feature was not available on my account. Therefore, I did not claim that an AI-generated PR summary had been produced.

Instead, I manually drafted the PR description.

### Final PR description

```markdown
## Summary

- Completed `getOrderById` and `createOrder` in `OrderController`.
- Added the `UserDTO` record fields and `fromUser` mapper.
- Added a small `main` method to verify the `UserDTO` mapping.
- Verified the implementation with the provided JUnit tests.

## Purpose

I made these changes to complete the Module 3 hands-on exercises, evaluate AI-generated Java boilerplate, and verify the generated implementation through compilation and tests.

## Notes

GitHub Copilot's PR Summary feature was not available on my account, so this PR description was manually drafted instead.
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

### Copilot suggestion

```text
feat/close-account-permanently
```

### Evaluation

I agree with the suggestion. It follows the required `prefix/short-kebab-slug` format, uses an allowed `feat` prefix, and clearly describes the requested feature.

I did not create this branch because Part E only required evaluating the suggested branch name.

---

## Reflection

AI was useful for generating repetitive Java boilerplate and filling in straightforward implementations. After the relevant context from `User.java` was available, Copilot correctly inferred the `UserDTO` fields and generated the mapper using the existing getter methods. It also correctly completed the simple `getOrderById` lookup and the `createOrder` logic in `OrderController`.

However, the first `UserDTO` completion showed why AI-generated code must be reviewed before accepting it. Without the context of `User.java`, Copilot suggested fields such as `username`, `firstName`, and `lastName`, along with getter methods that did not exist in the project. The suggestion looked plausible but was incorrect for this codebase.

The main lesson from the exercise is to treat AI suggestions as drafts rather than automatically correct code. I checked the generated code against the existing classes, compiled it, and ran the tests before considering the implementation complete. The same principle applied to the AI-generated commit message: I reviewed it and added my own explanation of why the changes were made.

For the PR summary, the GitHub Copilot feature was unavailable on my account, so I documented that limitation instead of presenting a manually written summary as AI-generated.
