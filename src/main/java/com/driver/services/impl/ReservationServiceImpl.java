package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

         Reservation reservation = new Reservation();
         if(userRepository3.findById(userId) == null)
         {
             throw new Exception("Cannot make reservation");
         }
         User user= userRepository3.findById(userId).get();
         if(parkingLotRepository3.findById(parkingLotId) == null)
         {
             throw new Exception("Cannot make reservation");
         }

         ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
         List<Spot> spotList = parkingLot.getSpotList();
         Spot optimalSpot = null;
         int optimalPrice = Integer.MIN_VALUE;
         for(Spot spot : spotList)
         {
             if(spot.getOccupied().equals(false))
             {
                 if(spot.getSpotType().equals(SpotType.TWO_WHEELER))
                 {
                     if(numberOfWheels <= 2)
                     {
                         if(optimalPrice > spot.getPricePerHour())
                         {
                             optimalPrice = spot.getPricePerHour();
                             optimalSpot = spot;
                         }
                     }
                 }
                 else if(spot.getSpotType().equals(SpotType.FOUR_WHEELER))
                 {
                     if(numberOfWheels <= 4)
                     {
                         if(optimalPrice > spot.getPricePerHour())
                         {
                             optimalPrice = spot.getPricePerHour();
                             optimalSpot = spot;

                         }
                     }
                 }
                 else {
                     if(optimalPrice > spot.getPricePerHour())
                     {
                         optimalPrice = spot.getPricePerHour();
                         optimalSpot = spot;
                     }
                 }

             }
         }

         if(optimalSpot == null)
         {
             throw new Exception("Cannot make reservation");
         }

         reservation.setUser(user);
         reservation.setSpot(optimalSpot);
         reservation.setNumberOfHours(timeInHours);
         List<Reservation> reservations = user.getReservationList();
         List<Reservation> reservationList = optimalSpot.getReservationList();
         reservations.add(reservation);
         user.setReservationList(reservationList);
         optimalSpot.setReservationList(reservationList);
         userRepository3.save(user);
         spotRepository3.save(optimalSpot);
         return reservation;


        return reservation;
    }
}
