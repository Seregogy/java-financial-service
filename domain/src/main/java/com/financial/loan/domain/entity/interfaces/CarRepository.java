package com.financial.loan.domain.entity.interfaces;

import com.financial.loan.domain.entity.entity.Car;

import java.util.List;
import java.util.UUID;

public interface CarRepository
{
    public UUID create(Car entity) ;

    public List<Car> getAll();

    public Car  getByLoan(UUID idLoan);

    public UUID Delete(UUID idCar);

    public UUID Update(UUID idCar , Car car );

}
