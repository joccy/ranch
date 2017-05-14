package org.lpw.ranch.account.type;

import org.lpw.ranch.account.AccountModel;
import org.lpw.ranch.account.log.LogModel;
import org.lpw.ranch.account.log.LogService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lpw
 */
@Service(AccountTypeSupport.NAME + AccountTypes.DEPOSIT)
public class DepositImpl extends AccountTypeSupport implements AccountType {
    @Override
    public String getName() {
        return AccountTypes.DEPOSIT;
    }

    @Override
    public String change(AccountModel account, int amount, Map<String, String> map) {
        account.setPending(account.getPending() + amount);

        return log(account, amount, LogService.State.New, map);
    }

    @Override
    public void complte(AccountModel account, LogModel log) {
        if (account.getPending() < log.getAmount())
            return;

        account.setPending(account.getPending() - log.getAmount());
        if (log.getState() == LogService.State.Reject.ordinal())
            return;

        account.setBalance(account.getBalance() + log.getAmount());
        account.setDeposit(account.getDeposit() + log.getAmount());
        log.setBalance(account.getBalance());
    }
}