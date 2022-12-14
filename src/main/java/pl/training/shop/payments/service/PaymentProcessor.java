package pl.training.shop.payments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.training.shop.payments.service.input.PaymentService;
import pl.training.shop.payments.service.model.PaymentIdDomain;
import pl.training.shop.payments.service.model.PaymentDomain;
import pl.training.shop.payments.service.model.PaymentRequestDomain;
import pl.training.shop.payments.service.model.PaymentStatusDomain;
import pl.training.shop.payments.service.output.PaymentRepository;
import pl.training.shop.payments.service.output.TimeProvider;

@Transactional
@Service("paymentService")
@Log
@RequiredArgsConstructor
public class PaymentProcessor implements PaymentService {

    private final PaymentIdGenerator paymentIdGenerator;
    private final PaymentFeeCalculator paymentFeeCalculator;
    private final PaymentRepository paymentsRepository;
    private final TimeProvider timeProvider;

    @Override
    public PaymentDomain process(PaymentRequestDomain paymentRequest) {
        var paymentValue = calculatePaymentValue(paymentRequest.getValue());
        var payment = createPayment(paymentValue);
        return paymentsRepository.save(payment);
    }

    private PaymentDomain createPayment(Money paymentValue) {
        return PaymentDomain.builder()
                .id(paymentIdGenerator.getNext())
                .value(paymentValue)
                .timestamp(timeProvider.getTimestamp())
                .status(PaymentStatusDomain.STARTED)
                .build();
    }

    private Money calculatePaymentValue(Money paymentValue) {
        var paymentFee = paymentFeeCalculator.calculateFee(paymentValue);
        return paymentValue.add(paymentFee);
    }

    @Override
    public PaymentDomain getById(PaymentIdDomain paymentIdDomain) {
        return paymentsRepository.getById(paymentIdDomain)
                .orElseThrow(PaymentNotFoundException::new);
    }

}
