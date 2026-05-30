package com.financial.loan.domain.domainexception;

import com.financial.loan.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContextualUnsupportedRoleException extends RuntimeException {
  private Role expectedRole;
  private Role actualRole;
}
