package com.appealing.activities;

import com.appealing.model.Identifier;
import com.appealing.model.Appeal;
import com.appealing.model.AppealStatus;
import com.appealing.repositories.AppealRepository;
import com.appealing.representations.AppealRepresentation;

public class CompleteAppealActivity {

    public AppealRepresentation completeAppeal(Identifier id) {
        AppealRepository repository = AppealRepository.current();
        if (repository.has(id)) {
            Appeal appeal = repository.get(id);

            if (appeal.getStatus() == AppealStatus.POSITIVE || appeal.getStatus() == AppealStatus.NEGATIVE) {
                appeal.setStatus(AppealStatus.REVIEWED);
            } else if (appeal.getStatus() == AppealStatus.REVIEWED) {
                throw new AppealAlreadyCompletedException();
            } else if (appeal.getStatus() == AppealStatus.UNREVIEWED) {
                throw new AppealNotReviewedException();
            }

            return new AppealRepresentation(appeal);
        } else {
            throw new NoSuchAppealException();
        }
    }
}
